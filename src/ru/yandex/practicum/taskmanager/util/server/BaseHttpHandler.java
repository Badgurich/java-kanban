package ru.yandex.practicum.taskmanager.util.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
		protected final TaskManager tm;
		Gson gson = new GsonBuilder()
						.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
						.registerTypeAdapter(Duration.class, new DurationAdapter())
						.create();

		public BaseHttpHandler(TaskManager tm) {
				this.tm = tm;
		}

		protected void sendText(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(200, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}

		protected void sendNotFound(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(404, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}

		protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(406, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}

		protected void sendCreated(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(201, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}

		protected void sendMethodNotAllowed(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(405, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}

		protected void sendBadRequest(HttpExchange h, String text) throws IOException {
				byte[] resp = text.getBytes(StandardCharsets.UTF_8);
				h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
				h.sendResponseHeaders(400, resp.length);
				h.getResponseBody().write(resp);
				h.close();
		}
} 