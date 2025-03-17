package ru.yandex.practicum.taskmanager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.servers.HttpTaskServer;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Managers;
import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;
import ru.yandex.practicum.taskmanager.util.json.SubtaskListTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtasksHandlerTest {
    TaskManager tm = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(tm);
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    void setUp() throws IOException {
        tm.removeAllTasks();
        tm.removeAllSubtasks();
        tm.removeAllEpics();
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testAddSubTask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Первый эпик");
        tm.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), epic1.getTaskId());
        String taskJson = gson.toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = tm.getSubtaskList();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Сабтаск 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Первый эпик");
        tm.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), epic1.getTaskId());
        Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", 3, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 31), epic1.getTaskId());
        tm.addSubtask(subtask1);
        tm.addSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> tasksFromResponse = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
        assertNotNull(tasksFromResponse, "Задачи не возвращаются");
        assertEquals(2, tasksFromResponse.size(), "Некорректное количество задач");
        assertEquals(tm.getSubtaskList(), tasksFromResponse);
    }

    @Test
    void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Первый эпик");
        tm.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), 1);
        tm.addTask(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + subtask1.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromResponse = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(taskFromResponse, "Задача не возвращается");
        assertEquals(subtask1, taskFromResponse);
    }

    @Test
    void testDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Первый эпик");
        tm.addEpic(epic1);
        Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), 1);
        tm.addSubtask(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask1.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task taskFromManager = tm.getTask(subtask1.getTaskId());
        assertNull(taskFromManager, "Задача возвращается");
    }

    @Test
    void testGetNonExistentSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}