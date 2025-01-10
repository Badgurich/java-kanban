package ru.yandex.practicum.taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.util.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
		@Test
		public void taskEqualsTaskIfIdEqualsId() {
				Task task1 = new Task("Задача 1", "Первая задача", 1, Status.NEW);
				Task task2 = new Task("Задача 2", "Вторая задача", 1, Status.NEW);
				assertEquals(task1, task2);
		}
}