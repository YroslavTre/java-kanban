package test.http;

import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicsHandlerTest extends HttpTaskServerTestBase {

    @Test
    void createEpicReturn201() throws Exception {
        // Arrange
        Epic epic = new Epic("Эпик", "Описание эпика",0, Status.NEW);
        String json = gson.toJson(epic);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(201, response.statusCode());
        assertEquals(1, manager.getEpics().size());
    }

    @Test
    void getEpicSubtasksReturn200() throws Exception {
        // Arrange
        Epic epic = new Epic("Эпик", "Описание Эпика", 0, Status.NEW);  // Добавлен параметр id
        manager.createEpic(epic);

        // Используем правильный конструктор Subtask
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", 0, Status.NEW,
                epic.getId(), LocalDateTime.now(), Duration.ofMinutes(30));
        manager.createSubtask(subtask);

        // Act
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);
        assertEquals(1, subtasks.length);
    }
}