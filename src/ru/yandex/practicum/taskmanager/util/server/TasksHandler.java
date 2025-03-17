package ru.yandex.practicum.taskmanager.util.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.exceptions.TimeValidationException;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public TasksHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_TASKS: {
                handleGetTasks(exchange);
                break;
            }
            case GET_TASKS_ID: {
                handleGetTasksById(exchange);
                break;
            }
            case POST_TASKS: {
                handlePostTasks(exchange);
                break;
            }
            case DELETE_TASKS_ID: {
                handleDeleteTasks(exchange);
                break;
            }
            default:
                sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Нет такого эндпоинта.", exchange)));
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> taskList = tm.getTaskList();
        String response = gson.toJson(taskList);
        if (!taskList.isEmpty()) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задачи не найдены.")));
        }
    }

    private void handleGetTasksById(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        Task task = tm.getTask(id);
        String response = gson.toJson(task);
        if (task != null) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException, TimeValidationException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            tm.addTask(task);
            sendCreated(exchange, gson.toJson(new BaseResponse("201", "Задача создана.")));
        } catch (TimeValidationException e) {
            sendHasInteractions(exchange, gson.toJson(new BaseResponse("406", e.getMessage())));
        }
    }

    private void handleDeleteTasks(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        if (tm.getTaskList().contains(tm.getTask(id))) {
            tm.removeTask(id);
            sendText(exchange, gson.toJson(new BaseResponse("200", "Задача удалена.")));
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }
}

