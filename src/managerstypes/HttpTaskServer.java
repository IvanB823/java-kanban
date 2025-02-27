package managerstypes;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import taskstypes.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        gson = getGson();
        this.taskManager = taskManager;

        server.createContext("/tasks", new TasksHttpHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtasksHttpHandler(taskManager, gson));
        server.createContext("/epics", new EpicsHttpHandler(taskManager, gson));
        server.createContext("/history", new HistoryHttpHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedHttpHandler(taskManager, gson));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        Task task = new Task("Имя задачи 1", "Описание задачи 1",
                Duration.ofMinutes(55),
                LocalDateTime.of(2025, 2, 7, 16, 27));
        Task task2 = new Task("Имя задачи 2", "Описание задачи 2",
                Duration.ofMinutes(55),
                LocalDateTime.of(2025, 2, 7, 17, 27));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println(response.body());
        httpTaskServer.stop();
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен!");
    }
}
