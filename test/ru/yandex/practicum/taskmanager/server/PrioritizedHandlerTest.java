package ru.yandex.practicum.taskmanager.server;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHandlerTest extends HandlerSetUpAndTearDown {

		@Test
		void testGetEmptyPrioritizedList() throws IOException, InterruptedException {
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/prioritized");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(404, response.statusCode());
		}

		@Test
		void testGetPrioritizedList() throws IOException, InterruptedException {
				Epic epic1 = new Epic("Эпик 1", "Первый эпик");
				tm.addEpic(epic1);
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
				tm.addTask(task1);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", 2, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2001, 1, 1, 1, 1), 1);
				tm.addSubtask(subtask1);
				HttpClient client = HttpClient.newHttpClient();
				URI url = URI.create("http://localhost:8080/prioritized");
				HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				assertEquals(200, response.statusCode());
				String prioritizedToJson = gson.toJson(tm.getPrioritizedTasks());
				assertEquals(prioritizedToJson, response.body());
		}
}