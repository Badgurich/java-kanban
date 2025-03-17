package ru.yandex.practicum.taskmanager.util.server;

import com.sun.net.httpserver.HttpExchange;

public class BaseResponse {
    String status;
    String message;
    String requestPath;

    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(String status, String message, HttpExchange exchange) {
        this.status = status;
        this.message = message;
        this.requestPath = exchange.getRequestURI().toString();
    }
}
