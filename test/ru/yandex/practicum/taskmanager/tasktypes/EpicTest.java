package ru.yandex.practicum.taskmanager.tasktypes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
		@Test
		public void epicEqualsEpicIfIdEqualsId() {
				Epic epic1 = new Epic("Эпик 1", "Первая задача", 1);
				Epic epic2 = new Epic("Эпик 2", "Вторая задача", 1);
				assertEquals(epic1, epic2);
		}
}