package ru.yandex.practicum.taskmanager.util.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.taskmanager.exceptions.TimeValidationException;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.taskmanager.util.server.EndpointGetter.getEndpoint;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public EpicsHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS: {
                handleGetEpics(exchange);
                break;
            }
            case GET_EPICS_ID: {
                handleGetEpicsById(exchange);
                break;
            }
            case POST_EPICS: {
                handlePostEpics(exchange);
                break;
            }
            case DELETE_EPICS_ID: {
                handleDeleteEpics(exchange);
                break;
            }
            default:
                sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Нет такого эндпоинта.", exchange)));
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epicList = tm.getEpicList();
        String response = gson.toJson(epicList);
        if (!epicList.isEmpty()) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задачи не найдены.")));
        }
    }

    private void handleGetEpicsById(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        Epic epic = tm.getEpic(id);
        String response = gson.toJson(epic);
        if (epic != null) {
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }

    private void handlePostEpics(HttpExchange exchange) throws IOException, TimeValidationException {
        try {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            tm.addEpic(epic);
            sendCreated(exchange, gson.toJson(new BaseResponse("201", "Задача создана.")));
        } catch (TimeValidationException e) {
            sendHasInteractions(exchange, gson.toJson(new BaseResponse("406", e.getMessage())));
        }
    }

    private void handleDeleteEpics(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(splitPath[2]);
        if (tm.getEpicList().contains(tm.getEpic(id))) {
            tm.removeEpic(id);
            sendText(exchange, gson.toJson(new BaseResponse("200", "Задача удалена.")));
        } else {
            sendNotFound(exchange, gson.toJson(new BaseResponse("404", "Задача не найдена.")));
        }
    }
}

