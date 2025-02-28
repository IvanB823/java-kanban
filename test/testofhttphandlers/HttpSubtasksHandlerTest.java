package testofhttphandlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import httpserver.HttpTaskServer;
import managerstypes.Managers;
import managerstypes.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;
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

public class HttpSubtasksHandlerTest {

    private SubTask subTask;
    private final TaskManager taskManager = Managers.getDefault();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpSubtasksHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
        httpTaskServer.start();
        Epic epic = new Epic("Имя эпика1", "Описание эпика1");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Имя подзадачи 1", "Описание подзадачи 1", StatusOfTask.NEW,
                1, epic.getId(), Duration.ofMinutes(55),
                LocalDateTime.of(2025, 2, 7, 16, 27));
        taskManager.addSubTask(subTask);
    }

    @AfterEach
    void serverStop() {
        httpTaskServer.stop();
    }

    @Test
    void getSubtasksTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> subTasks = gson.fromJson(response.body(), taskType);

        assertNotNull(subTasks);
        assertEquals(1, subTasks.size());
        assertEquals("Имя подзадачи 1", subTasks.getFirst().getTaskName());
    }

    @Test
    void getSubtaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {}.getType();
        SubTask actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual);
        assertEquals("Имя подзадачи 1", actual.getTaskName());
    }

    @Test
    void postSubtaskTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        SubTask subTask2 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", Duration.ofMinutes(55),
                LocalDateTime.of(2025, 2, 7, 17, 27),1);
        String json = gson.toJson(subTask2);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());
        assertEquals("Имя подзадачи 2", taskManager.getSubTask(3).getTaskName());
        assertEquals("Описание подзадачи 2", taskManager.getSubTask(3).getDescription());
    }

    @Test
    void postSubtaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        SubTask subTask2 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", StatusOfTask.DONE,
                2, 1, Duration.ofMinutes(55), LocalDateTime.of(2025, 2, 7, 16, 27));
        String json = gson.toJson(subTask2);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(201, response.statusCode());
        assertEquals("Имя подзадачи 2", taskManager.getSubTask(2).getTaskName());
        assertEquals("Описание подзадачи 2", taskManager.getSubTask(2).getDescription());
        assertEquals(2, taskManager.getSubTask(2).getId());
        assertEquals("DONE", taskManager.getSubTask(2).getStatus().toString());
    }

    @Test
    void deleteSubtaskByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode());
    }
}
