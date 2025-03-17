package ru.yandex.practicum.taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    public void taskEqualsTaskIfIdEqualsId() {
        Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
        Task task2 = new Task("Задача 2", "Вторая задача", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.of(2000, 1, 1, 1, 1));
        assertEquals(task1, task2);
    }
}