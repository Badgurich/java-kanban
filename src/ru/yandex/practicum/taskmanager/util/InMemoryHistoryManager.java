package ru.yandex.practicum.taskmanager.util;

import ru.yandex.practicum.taskmanager.interfaces.HistoryManager;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
		private ArrayList<Task> history = new ArrayList<>();

		@Override
		public void add(Task task) {
				history.add(task);
				if (history.size() > 10) {
						history.removeFirst();
				}
		}

		@Override
		public ArrayList<Task> getHistory() {
				return history;
		}
}
