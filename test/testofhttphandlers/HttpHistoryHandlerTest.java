package testofhttphandlers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import httpServer.HttpTaskServer;
import managerstypes.Managers;
import managerstypes.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.StatusOfTask;
import taskstypes.Task;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpHistoryHandlerTest {

    private final TaskManager taskManager = Managers.getDefault();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpHistoryHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
        httpTaskServer.start();
        Task task = new Task("Задача 1", "Описание задачи 1", StatusOfTask.NEW, 1, Duration.ofMinutes(12),
                LocalDateTime.of(2024, 12, 3, 22, 12));
        Task task2 = new Task("Задача 2", "Описание задачи 2", StatusOfTask.NEW, 2, Duration.ofMinutes(36),
                LocalDateTime.of(2025, 3, 9, 11, 25));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task.getId());
    }

    @AfterEach
    void serverStop() {
        httpTaskServer.stop();
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> history = gson.fromJson(response.body(), taskType);

        assertNotNull(history, "Задачи не возвращаются");
        assertEquals(2, history.size());
        assertEquals("Задача 2", history.get(0).getTaskName());
        assertEquals("Задача 1", history.get(1).getTaskName());
    }
}

