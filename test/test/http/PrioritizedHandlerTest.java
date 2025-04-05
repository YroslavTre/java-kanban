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

class PrioritizedHandlerTest extends HttpTaskServerTestBase {

    @Test
    void getPrioritizedTasksReturn200A() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Используем правильный конструктор Task с id=0 (новые задачи)
        Task task1 = new Task("Задача 1", "Описание задачи 1", 0, Status.NEW,
                now.plusHours(1), Duration.ofMinutes(30));
        Task task2 = new Task("Задача 2", "Описание задачи 2", 0, Status.NEW,
                now, Duration.ofMinutes(30));

        manager.createTask(task1);
        manager.createTask(task2);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length);
        assertEquals("Задача 2", tasks[0].getTitle()); // Должен быть первым, так как раньше по времени
    }
}