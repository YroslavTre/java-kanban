package main.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
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
                        // GET /subtasks - получить все подзадачи
                        List<Subtask> subtasks = taskManager.getSubtasks();
                        sendSuccess(exchange, gson.toJson(subtasks));
                    } else if (pathParts.length == 3) {
                        // GET /subtasks/{id} - получить подзадачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask != null) {
                            sendSuccess(exchange, gson.toJson(subtask));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    // POST /subtasks - создать/обновить подзадачу
                    String requestBody = readRequest(exchange);
                    Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                    if (subtask.getId() == 0) {
                        try {
                            taskManager.createSubtask(subtask);
                            sendCreated(exchange, gson.toJson(subtask));
                        } catch (IllegalArgumentException e) {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        taskManager.updateSubtask(subtask);
                        sendCreated(exchange, gson.toJson(subtask));
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /subtasks/{id} - удалить подзадачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteSubtask(id);
                        sendSuccess(exchange, "Subtask deleted");
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