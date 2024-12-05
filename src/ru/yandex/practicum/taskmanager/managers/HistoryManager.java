package ru.yandex.practicum.taskmanager.managers;

import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.List;

public interface HistoryManager {
		void add(Task task);

		void remove(int id);

		List<Task> getHistory();
}