package ru.yandex.practicum.taskmanager.tasktypes;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.enums.Status;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
		@Test
		public void epicEqualsEpicIfIdEqualsId() {
				Epic epic1 = new Epic("Эпик 1", "Первая задача", 1, Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Вторая задача", 1, Status.NEW);
				assertEquals(epic1, epic2);
		}
}