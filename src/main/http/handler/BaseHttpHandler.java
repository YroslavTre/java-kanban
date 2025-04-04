package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendSuccess(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 200);
    }

    protected void sendCreated(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 201);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String response = "Not Found";
        sendText(exchange, response, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Task has time interactions with existing tasks";
        sendText(exchange, response, 406);
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        String response = "Internal Server Error";
        sendText(exchange, response, 500);
    }

    protected String readRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}