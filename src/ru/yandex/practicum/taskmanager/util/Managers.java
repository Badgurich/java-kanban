package ru.yandex.practicum.taskmanager.util;

import ru.yandex.practicum.taskmanager.managers.HistoryManager;
import ru.yandex.practicum.taskmanager.managers.TaskManager;

public class Managers {
		public static TaskManager getDefault() {
				return new InMemoryTaskManager();
		}

		public static HistoryManager getDefaultHistory() {
				return new InMemoryHistoryManager();
		}
}