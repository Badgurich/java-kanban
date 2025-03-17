package ru.yandex.practicum.taskmanager.server;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.json.TaskListTypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TasksHandlerTest extends HandlerSetUpAndTearDown {

		@Test
		void testAddTask() throws IOException, InterruptedException {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				String taskJson = gson.toJson(task1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks");
				HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(201, response.statusCode());
				List<Task> tasksFromManager = tm.getTaskList();
				assertNotNull(tasksFromManager, "Задачи не возвращаются");
				assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
				assertEquals("Задача 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
		}

		@Test
		void testUpdateTask() throws IOException, InterruptedException {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				Task task2 = new Task("Задача 2", "Первая задача+", 1, Status.IN_PROGRESS, Duration.ofMinutes(30), LocalDateTime.of(2001, 1, 1, 1, 1));
				tm.addTask(task1);
				String taskJson = gson.toJson(task2);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks/1");
				HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(201, response.statusCode());
				List<Task> tasksFromManager = tm.getTaskList();
				assertNotNull(tasksFromManager, "Задачи не возвращаются");
				assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
				assertEquals("Задача 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
		}

		@Test
		void testGetTasks() throws IOException, InterruptedException {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				Task task2 = new Task("Задача 2", "Вторая задача", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 2, 1, 1));
				tm.addTask(task1);
				tm.addTask(task2);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				List<Task> tasksFromResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());
				assertNotNull(tasksFromResponse, "Задачи не возвращаются");
				assertEquals(2, tasksFromResponse.size(), "Некорректное количество задач");
				assertEquals(tm.getTaskList(), tasksFromResponse);
		}

		@Test
		void testGetTaskById() throws IOException, InterruptedException {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				tm.addTask(task1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks/" + task1.getTaskId());
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				Task taskFromResponse = gson.fromJson(response.body(), Task.class);
				assertNotNull(taskFromResponse, "Задача не возвращается");
				assertEquals(task1, taskFromResponse);
		}

		@Test
		void testDeleteTaskById() throws IOException, InterruptedException {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				tm.addTask(task1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks/" + task1.getTaskId());
				HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				Task taskFromManager = tm.getTask(task1.getTaskId());
				assertNull(taskFromManager, "Задача возвращается");
		}

		@Test
		void testGetNonExistentTask() throws IOException, InterruptedException {
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/tasks/1");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(404, response.statusCode());
		}
}