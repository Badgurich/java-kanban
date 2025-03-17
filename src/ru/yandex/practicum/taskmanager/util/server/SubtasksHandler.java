package ru.yandex.practicum.taskmanager.util.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.exceptions.LinkedEpicValidationException;
import ru.yandex.practicum.taskmanager.exceptions.TimeValidationException;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public SubtasksHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS: {
                handleGetSubtasks(exchange);
                break;
            }
            case GET_SUBTASKS_ID: {
                handleGetSubtasksById(exchange);
                break;
            }
            case POST_SUBTASKS: {
                handlePostSubtasks(exchange);
                break;
            }
            case DELETE_SUBTASKS_ID: {
                handleDeleteSubtasks(exchange);
                break;
            }
            default:
                sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Нет такого эндпоинта.", exchange)));
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtaskList = tm.getSubtaskList();
        String response = gson.toJson(subtaskList);
        if (!subtaskList.isEmpty()) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задачи не найдены.")));
        }
    }

    private void handleGetSubtasksById(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        Subtask subtask = tm.getSubtask(id);
        String response = gson.toJson(subtask);
        if (subtask != null) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }

    private void handlePostSubtasks(HttpExchange exchange) throws IOException, TimeValidationException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            tm.addSubtask(subtask);
            sendCreated(exchange, gson.toJson(new BaseResponse("201", "Задача создана.")));
        } catch (TimeValidationException e) {
            sendHasInteractions(exchange, gson.toJson(new BaseResponse("406", e.getMessage())));
        } catch (LinkedEpicValidationException ex) {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", ex.getMessage())));
        }
    }

    private void handleDeleteSubtasks(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        if (tm.getSubtaskList().contains(tm.getSubtask(id))) {
            tm.removeSubtask(id);
            sendText(exchange, gson.toJson(new BaseResponse("200", "Задача удалена.")));
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }
}

