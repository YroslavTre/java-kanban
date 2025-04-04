package main.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager, Gson gson) {
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
                        // GET /tasks - получить все задачи
                        List<Task> tasks = taskManager.getTasks();
                        sendSuccess(exchange, gson.toJson(tasks));
                    } else if (pathParts.length == 3) {
                        // GET /tasks/{id} - получить задачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            sendSuccess(exchange, gson.toJson(task));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    // POST /tasks - создать/обновить задачу
                    String requestBody = readRequest(exchange);
                    Task task = gson.fromJson(requestBody, Task.class);
                    if (task.getId() == 0) {
                        try {
                            taskManager.createTask(task);
                            sendCreated(exchange, gson.toJson(task));
                        } catch (IllegalArgumentException e) {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        taskManager.updateTask(task);
                        sendCreated(exchange, gson.toJson(task));
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /tasks/{id} - удалить задачу по ID
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTask(id);
                        sendSuccess(exchange, "Task deleted");
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