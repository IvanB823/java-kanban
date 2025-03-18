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
public class HttpEpicsHandlerTest {
    private final TaskManager taskManager = Managers.getDefault();
    private final HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpEpicsHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager.removeAllTasks();
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        httpTaskServer.start();
        Epic epic = new Epic("Эпик 1", "Описание эпика1", 1, new ArrayList<>());
        taskManager.addEpic(epic);
    }

    @AfterEach
    void serverStop() {
        httpTaskServer.stop();
    }

    @Test
    void getEpicsTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
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
        List<Epic> epics = gson.fromJson(response.body(), taskType);

        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("Эпик 1", epics.getFirst().getTaskName());
    }

    @Test
    void getSubtasksByEpicIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи1", Duration.ofMinutes(29),
                LocalDateTime.of(2024, 12, 7, 21, 11), 1);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи2", Duration.ofMinutes(17),
                LocalDateTime.of(2025, 2, 7, 17, 27), 1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        assertEquals(200, response.statusCode(), "Ожидается код статуса - 200");

        Type taskType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> subTasks = gson.fromJson(response.body(), taskType);

        assertNotNull(subTasks);
        assertEquals(2, subTasks.size());
        assertEquals("Подзадача 1", subTasks.get(0).getTaskName());
        assertEquals("Подзадача 2", subTasks.get(1).getTaskName());
    }

    @Test
    void getEpicByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1");
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
        Epic actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Эпик не возвращается");
        assertEquals("Эпик 1", actual.getTaskName());
    }

    @Test
    void postEpicTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        String json = gson.toJson(epic2);
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
        assertEquals("Эпик 2", taskManager.getEpic(2).getTaskName());
        assertEquals("Описание эпика 2", taskManager.getEpic(2).getDescription());
    }

    @Test
    void deleteEpicByIdTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/epics/1");
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

