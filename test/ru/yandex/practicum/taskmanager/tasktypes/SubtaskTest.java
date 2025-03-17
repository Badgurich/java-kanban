package ru.yandex.practicum.taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.util.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
		@Test
		public void subtaskEqualsSubtaskIfIdEqualsId() {
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая задача", 1, Status.NEW, 2);
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая задача", 1, Status.NEW, 4);
				assertEquals(subtask1, subtask2);
		}
}