package ru.yandex.practicum.taskmanager.interfaces;

import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.ArrayList;

public interface HistoryManager {
		void add(Task task);

		ArrayList<Task> getHistory();
}
