package ru.yandex.practicum.taskmanager.util.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

		public PrioritizedHandler(TaskManager tm) {
				super(tm);
		}

		@Override
		public void handle(HttpExchange exchange) throws IOException {
				Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

				if (Objects.requireNonNull(endpoint) == Endpoint.GET_PRIORITIZED) {
						handleGetPrioritized(exchange);
				} else {
						sendMethodNotAllowed(exchange, gson.toJson(new BaseResponse("405", "Нет такого эндпоинта.", exchange)));
				}
		}

		private void handleGetPrioritized(HttpExchange exchange) throws IOException {
				List<Task> sortedTaskList = tm.getPrioritizedTasks();
				String response = gson.toJson(sortedTaskList);
				if (!sortedTaskList.isEmpty()) {
						sendText(exchange, response);
				} else {
						sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Сортированный спиок пуст.")));
				}
		}
}


