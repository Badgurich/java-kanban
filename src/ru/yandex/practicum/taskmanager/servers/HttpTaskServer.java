package ru.yandex.practicum.taskmanager.servers;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.util.Managers;
import ru.yandex.practicum.taskmanager.util.server.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final TaskManager tm = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(tm));
        httpServer.createContext("/subtasks", new SubtasksHandler(tm));
        httpServer.createContext("/epics", new EpicsHandler(tm));
        httpServer.createContext("/history", new HistoryHandler(tm));
        httpServer.createContext("/prioritized", new PrioritizedHandler(tm));
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
