package ru.yandex.practicum.taskmanager.util.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
		private final TaskManager tm;
		Gson gson = new GsonBuilder()
						.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
						.registerTypeAdapter(Duration.class, new DurationAdapter())
						.create();

		public HistoryHandler(TaskManager tm) {
				this.tm = tm;
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
				Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

				if (Objects.requireNonNull(endpoint) == Endpoint.GET_HISTORY) {
						handleGetHistory(exchange);
				} else {
						sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Нет такого эндпоинта.", exchange)));
				}
		}

		private void handleGetHistory(HttpExchange exchange) throws IOException {
				List<Task> history = tm.getHistory();
				String response = gson.toJson(history);
				if (!history.isEmpty()) {
						sendText(exchange, response);
				} else {
						sendNotFound(exchange, gson.toJson(new BaseResponse("404", "История пуста.")));
				}
		}
}

