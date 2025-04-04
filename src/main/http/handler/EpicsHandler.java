package main.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        // GET /epics - получить все эпики
                        List<Epic> epics = taskManager.getEpics();
                        sendSuccess(exchange, gson.toJson(epics));
                    } else if (pathParts.length == 3 && pathParts[2].matches("\\d+")) {
                        // GET /epics/{id} - получить эпик по ID
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            sendSuccess(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange);
                        }
                    } else if (pathParts.length == 4 && pathParts[2].matches("\\d+")
                            && pathParts[3].equals("subtasks")) {
                        // GET /epics/{id}/subtasks - получить подзадачи эпика
                        int id = Integer.parseInt(pathParts[2]);
                        List<Subtask> subtasks = taskManager.getEpicSubtasks(id);
                        sendSuccess(exchange, gson.toJson(subtasks));
                    }
                    break;
                case "POST":
                    // POST /epics - создать/обновить эпик
                    String requestBody = readRequest(exchange);
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    if (epic.getId() == 0) {
                        taskManager.createEpic(epic);
                        sendCreated(exchange, gson.toJson(epic));
                    } else {
                        taskManager.updateEpic(epic);
                        sendCreated(exchange, gson.toJson(epic));
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /epics/{id} - удалить эпик по ID
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpic(id);
                        sendSuccess(exchange, "Epic deleted");
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}