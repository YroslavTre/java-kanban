package test.http;

import main.tasks.Status;
import main.tasks.Task;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TasksHandlerTest extends HttpTaskServerTestBase {

    @Test
    void createTaskReturn201() throws Exception {
        // Arrange
        Task task = new Task("Test", "Description", 0,  Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        String json = gson.toJson(task);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getTasks().size());
        assertEquals("Test", manager.getTasks().get(0).getTitle());
    }

    @Test
    void getTasksReturn200() throws Exception {
        // Arrange
        Task task = new Task("Test", "Description",0,  Status.NEW);
        manager.createTask(task);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length);
    }

    @Test
    void deleteTaskReturn200() throws Exception {
        // Arrange
        Task task = new Task("Test", "Description", 0,  Status.NEW);
        manager.createTask(task);
        int taskId = task.getId();

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size());
    }
}