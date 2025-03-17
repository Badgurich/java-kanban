package ru.yandex.practicum.taskmanager.server;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.json.EpicListTypeToken;
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

class EpicsHandlerTest extends HandlerSetUpAndTearDown {

		@Test
		void testAddEpic() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				String taskJson = gson.toJson(epic1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics");
				HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(201, response.statusCode());
				List<Epic> epicsFromManager = tm.getEpicList();
				assertNotNull(epicsFromManager, "Задачи не возвращаются");
				assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
				assertEquals("Эпик 1", epicsFromManager.getFirst().getName(), "Некорректное имя задачи");
		}

		@Test
		void testGetEpics() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				Epic epic2 = new Epic("Эпик 2", "Второй эпик");
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				List<Epic> epicsFromResponse = gson.fromJson(response.body(), new EpicListTypeToken().getType());
				assertNotNull(epicsFromResponse, "Задачи не возвращаются");
				assertEquals(2, epicsFromResponse.size(), "Некорректное количество задач");
				assertEquals(tm.getEpicList(), epicsFromResponse);
		}

		@Test
		void testGetEpicById() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				tm.addEpic(epic1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics/" + epic1.getTaskId());
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				Task taskFromResponse = gson.fromJson(response.body(), Epic.class);
				assertNotNull(taskFromResponse, "Задача не возвращается");
				assertEquals(epic1, taskFromResponse);
		}

		@Test
		void testDeleteEpicById() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				tm.addEpic(epic1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics/" + epic1.getTaskId());
				HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				Task taskFromManager = tm.getTask(epic1.getTaskId());
				assertNull(taskFromManager, "Задача возвращается");
		}

		@Test
		void testUpdateEpic() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				tm.addEpic(epic1);
				Epic epic2 = new Epic("Эпик 2", "Первый эпик+", 1);
				String epicJson = gson.toJson(epic2);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics/1");
				HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(201, response.statusCode());
				List<Epic> epicsFromManager = tm.getEpicList();
				assertNotNull(epicsFromManager, "Задачи не возвращаются");
				assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
				assertEquals("Эпик 2", epicsFromManager.getFirst().getName(), "Некорректное имя задачи");
		}

		@Test
		void testGetLinkedSubtasks() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				tm.addEpic(epic1);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", 3, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 31), epic1.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics/1/subtasks");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				List<Subtask> tasksFromResponse = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
				assertNotNull(tasksFromResponse, "Задачи не возвращаются");
				assertEquals(2, tasksFromResponse.size(), "Некорректное количество задач");
				assertEquals(tm.getSubtaskList(), tasksFromResponse);
		}

		@Test
		void testGetNonExistentEpic() throws IOException, InterruptedException {
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/epics/1");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(404, response.statusCode());
		}
}