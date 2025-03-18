package httpserver.httphandlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managerstypes.TaskManager;
import taskstypes.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHttpHandler(TaskManager taskManager, Gson gson) {
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
                        List<SubTask> subTasks = taskManager.getAllSubTasks();
                        String response = gson.toJson(subTasks);
                        sendJson(exchange, response);
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            SubTask subTask = taskManager.getSubTask(id);
                            String response = gson.toJson(subTask);
                            sendJson(exchange, response);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Подзадача не найдена, указан неверный ID подзадачи");
                        }
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    try {
                        if (subTask.getId() > 0) {
                            taskManager.updateSubTask(subTask);
                        } else {
                            taskManager.addSubTask(subTask);
                        }
                        sendText(exchange, "Подзадача сохранена успешно", 201);
                    } catch (JsonSyntaxException e) {
                        sendWrongRequest(exchange, "Некорректный формат JSON");
                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Подзадача пересекается с существующими задачами");
                    }
                    break;
                case "DELETE":
                    if (parts.length < 3) {
                        sendNotFound(exchange, "Подзадача не найдена, не указан ID подзадачи");
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            taskManager.removeSubTask(id);
                            sendText(exchange, "Подзадача удалена", 200);
                        } catch (RuntimeException e) {
                            sendNotFound(exchange, "Подзадача не найдена, указан неверный ID подзадачи");
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
