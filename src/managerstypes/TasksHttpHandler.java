package managerstypes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskstypes.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHttpHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHttpHandler(TaskManager taskManager, Gson gson) {
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
                        List<Task> tasks = taskManager.getAllTasks();
                        String response = gson.toJson(tasks);
                        sendJson(exchange, response);
                        System.out.println("Задачи получены.");
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            Task task = taskManager.getTask(id);
                            String response = gson.toJson(task);
                            sendJson(exchange, response);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Задача не найдена, указан неверный ID задачи");
                        }
                    }
                    break;
                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    try {
                        if (task.getId() > 0) {
                            taskManager.updateTask(task);
                        } else {
                            taskManager.addTask(task);
                        }
                        System.out.println("201. Задача сохранена.");
                        sendText(exchange, "Задача сохранена", 201);
                    } catch (JsonSyntaxException e) {
                        sendWrongRequest(exchange, "Некорректный формат JSON");
                    } catch (RuntimeException e) {
                        sendHasInteractions(exchange, "Задача пересекается с существующими задачами");
                    }
                    break;
                case "DELETE":
                    if (parts.length < 3) {
                        sendNotFound(exchange, "Задача не найдена, не указан ID задачи");
                    } else {
                        try {
                            int id = Integer.parseInt(parts[2]);
                            taskManager.removeTask(id);
                            sendText(exchange, "Задача удалена успешно", 200);
                        } catch (NumberFormatException e) {
                            sendNotFound(exchange, "Задача не найдена, указан неверный ID задачи");
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
