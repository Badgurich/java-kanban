package ru.yandex.practicum.taskmanager.managers;

import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.List;

public interface TaskManager {
		List<Task> getTaskList();

		List<Epic> getEpicList();

		List<Subtask> getSubtaskList();

		void removeAllTasks();

		void removeAllEpics();

		void removeAllSubtasks();

		Task getTask(int id);

		Epic getEpic(int id);

		Subtask getSubtask(int id);

		void addTask(Task task);

		void addEpic(Epic epic);

		void addSubtask(Subtask subtask);

		void updateTask(Task task);

		void updateEpic(Epic epic);

		void updateSubtask(Subtask subtask);

		void removeTask(int id);

		void removeEpic(int id);

		void removeSubtask(int id);

		List<Task> getHistory();

		List<Subtask> getLinkedSubtasks(Epic epic);

		List<Task> getPrioritizedTasks();
}