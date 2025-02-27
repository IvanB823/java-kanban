package managerstypes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskstypes.Epic;
import taskstypes.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicsHttpHandler extends BaseHttpHandler implements HttpHandler {

    public EpicsHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            switch (exchange.getRequestMethod()) {
                case "GET":
                    if (parts.length < 3) {
                        List<Epic> epics = taskManager.getAllEpics();
                        String response = gson.toJson(epics);
                        sendJson(exchange, response);
                    } else if (parts.length == 3) {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            Epic epic = taskManager.getEpic(id);
                            String response = gson.toJson(epic);
                            sendJson(exchange, response);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Эпик не найден, указан неверный ID эпика");
                        }
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            ArrayList<SubTask> subTasks = taskManager.getSubTasksForEpic(id);
                            String response = gson.toJson(subTasks);
                            sendJson(exchange, response);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Подзадачи эпика не найдены, указан неверный ID эпика");
                        }
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    try {
                        taskManager.addEpic(epic);
                        sendText(exchange, "Эпик сохранен успешно", 201);
                    } catch (JsonSyntaxException e) {
                        sendWrongRequest(exchange, "Некорректный формат JSON");
                    }
                    break;
                case "DELETE":
                    if (parts.length < 3) {
                        sendNotFound(exchange, "Эпик не найден, не указан ID эпика");
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            taskManager.removeEpic(id);
                            sendText(exchange, "Эпик удален успешно", 200);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Эпик не найден, указан неверный ID эпика");
                        }
                    }
                    break;
                default:
                    sendText(exchange, "Метод отсутствует", 405);
            }
        } catch (Exception e) {
            sendError(exchange, "Внутренняя ошибка сервера");
        } finally {
            exchange.close();
        }
    }
}
