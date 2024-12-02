package ru.yandex.practicum.taskmanager.util;

import ru.yandex.practicum.taskmanager.interfaces.HistoryManager;
import ru.yandex.practicum.taskmanager.interfaces.TaskManager;

public class Managers {
		public static TaskManager getDefault() {
				return new InMemoryTaskManager();
		}

		public static HistoryManager getDefaultHistory() {
				return new InMemoryHistoryManager();
		}
}