package test.http;

import main.tasks.Status;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryHandlerTest extends HttpTaskServerTestBase {

    @Test
    void getHistoryReturn200() throws Exception {
        // Arrange
        LocalDateTime time = LocalDateTime.now();

        Task task = new Task("Задача 1", "Описание задачи", 0, Status.NEW,
                time, Duration.ofHours(1));
        manager.createTask(task);
        manager.getTask(task.getId()); // Добавляем в историю

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, history.length);
    }
}