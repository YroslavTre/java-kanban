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

class NegativeTests extends HttpTaskServerTestBase {

    @Test
    void getNonExistentTaskReturn404() throws Exception {
        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(404, response.statusCode());
    }

    @Test
    void createTaskWithTimeConflictReturn406() throws Exception {
        // Arrange
        LocalDateTime time = LocalDateTime.now();

        // Используем правильный конструктор Task
        Task task1 = new Task("Задача 1", "Описание задачи", 0, Status.NEW,
                time, Duration.ofHours(1));
        manager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", 0, Status.NEW,
                time.plusMinutes(30), Duration.ofHours(1));
        String json = gson.toJson(task2);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(406, response.statusCode());
    }
}