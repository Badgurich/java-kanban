package ru.yandex.practicum.taskmanager.util.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

		public HistoryHandler(TaskManager tm) {
				super(tm);
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
				Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

				if (Objects.requireNonNull(endpoint) == Endpoint.GET_HISTORY) {
						handleGetHistory(exchange);
				} else {
						sendMethodNotAllowed(exchange, gson.toJson(new BaseResponse("405", "Нет такого эндпоинта.", exchange)));
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

