package httpServer.httpHandlers;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managerstypes.TaskManager;

public class BaseHttpHandler {

    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected void sendText(HttpExchange httpExchange, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(code, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendJson(HttpExchange exchange, String json) throws IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 404);
    }

    protected void sendError(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 500);
    }

    protected void sendHasInteractions(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 406);
    }

    protected void sendWrongRequest(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 400);
    }
}
