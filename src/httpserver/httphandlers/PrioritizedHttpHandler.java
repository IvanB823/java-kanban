package httpserver.httphandlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managerstypes.TaskManager;
import taskstypes.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHttpHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            if (exchange.getRequestMethod().equals("GET")) {
                List<Task> tasks = taskManager.getPrioritizedTasks();
                String response = gson.toJson(tasks);
                sendJson(exchange, response);
            } else {
                sendText(exchange, "Метод отсутствует", 405);
            }
        } catch (Exception e) {
            sendError(exchange, "Внутренняя ошибка сервера");
        } finally {
            exchange.close();
        }
    }
}
