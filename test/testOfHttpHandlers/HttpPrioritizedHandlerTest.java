package testOfHttpHandlers;
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

public class HttpPrioritizedHandlerTest {

    private final TaskManager taskManager = Managers.getDefault();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpPrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
        httpTaskServer.start();
        Task task = new Task("Задача 1", "Описание задачи 1", StatusOfTask.NEW, 1, Duration.ofMinutes(41),
                LocalDateTime.of(2025, 1, 2, 3, 27));
        Task task2 = new Task("Задача 2", "Описание задачи 2", StatusOfTask.NEW, 2, Duration.ofMinutes(13),
                LocalDateTime.of(2025, 5, 5, 11, 22));
        taskManager.addTask(task);
        taskManager.addTask(task2);
    }

    @AfterEach
    void serverStop() {
        httpTaskServer.stop();
    }

    @Test
    void getPrioritizedTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/prioritized");
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
        List<Task> prioritized = gson.fromJson(response.body(), taskType);

        assertNotNull(prioritized);
        assertEquals(2, prioritized.size());
        assertEquals("Задача 1", prioritized.get(0).getTaskName());
        assertEquals("Задача 2", prioritized.get(1).getTaskName());
    }
}
