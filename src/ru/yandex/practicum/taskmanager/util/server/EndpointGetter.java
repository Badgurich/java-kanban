package ru.yandex.practicum.taskmanager.util.server;

public class EndpointGetter {
    public static Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("GET")) {
            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_TASKS;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_TASKS_ID;
            }
            if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                return Endpoint.GET_SUBTASKS;
            }
            if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                return Endpoint.GET_SUBTASKS_ID;
            }
            if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                return Endpoint.GET_EPICS;
            }
            if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                return Endpoint.GET_EPICS_ID;
            }
            if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
                return Endpoint.GET_EPICS_ID_SUBTASKS;
            }
            if (pathParts.length == 2 && pathParts[1].equals("history")) {
                return Endpoint.GET_HISTORY;
            }
            if (pathParts.length == 2 && pathParts[1].equals("prioritized")) {
                return Endpoint.GET_PRIORITIZED;
            }
        }
        if (requestMethod.equals("POST")) {
            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.POST_TASKS;
            }
            if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                return Endpoint.POST_SUBTASKS;
            }
            if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                return Endpoint.POST_EPICS;
            }
        }
        if (requestMethod.equals("DELETE")) {
            if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                return Endpoint.DELETE_TASKS_ID;
            }
            if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                return Endpoint.DELETE_SUBTASKS_ID;
            }
            if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                return Endpoint.DELETE_EPICS_ID;
            }
        }
            return Endpoint.UNKNOWN;
    }
}
