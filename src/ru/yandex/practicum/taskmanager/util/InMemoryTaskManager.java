package ru.yandex.practicum.taskmanager.util;

import ru.yandex.practicum.taskmanager.enums.Status;
import ru.yandex.practicum.taskmanager.interfaces.HistoryManager;
import ru.yandex.practicum.taskmanager.interfaces.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
		private HashMap<Integer, Task> taskList = new HashMap<>();
		private HashMap<Integer, Epic> epicList = new HashMap<>();
		private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
		public int idCounter = 1;
		private HistoryManager history = Managers.getDefaultHistory();

		@Override
		public ArrayList<Task> getTaskList() {
				return new ArrayList<>(taskList.values());
		}

		@Override
		public ArrayList<Epic> getEpicList() {
				return new ArrayList<>(epicList.values());
		}

		@Override
		public ArrayList<Subtask> getSubtaskList() {
				return new ArrayList<>(subtaskList.values());
		}

		@Override
		public void removeAllTasks() {
				taskList.clear();

		}

		@Override
		public void removeAllEpics() {
				epicList.clear();
				subtaskList.clear();
		}

		@Override
		public void removeAllSubtasks() {
				subtaskList.clear();
				for (Epic epic : epicList.values()) {
						updateEpicStatus(epic);
				}
		}

		@Override
		public Task getTask(int id) {
				history.add(taskList.get(id));
				return taskList.get(id);
		}

		@Override
		public Epic getEpic(int id) {
				history.add(epicList.get(id));
				return epicList.get(id);
		}

		@Override
		public Subtask getSubtask(int id) {
				history.add(subtaskList.get(id));
				return subtaskList.get(id);
		}

		@Override
		public void addTask(Task task) {
				task.setTaskId(idCounter++);
				taskList.put(task.getTaskId(), task);
		}

		@Override
		public void addEpic(Epic epic) {
				epic.setTaskId(idCounter++);
				epicList.put(epic.getTaskId(), epic);
		}

		@Override
		public void addSubtask(Subtask subtask) {
				if (epicList.containsKey(subtask.getEpicId())) {
						subtask.setTaskId(idCounter++);
						subtaskList.put(subtask.getTaskId(), subtask);
						getEpicLocal(subtask.getEpicId()).linkedSubtasks.add(subtask);
						updateEpicStatus(getEpicLocal(subtask.getEpicId()));
				}
		}

		@Override
		public void updateTask(Task task) {
				if (taskList.containsKey(task.getTaskId())) {
						taskList.put(task.getTaskId(), task);
				}
		}

		@Override
		public void updateEpic(Epic epic) {
				if (epicList.containsKey(epic.getTaskId())) {
						epicList.put(epic.getTaskId(), epic);
				}
		}

		@Override
		public void updateSubtask(Subtask subtask) {
				if (subtaskList.containsKey(subtask.getTaskId())) {
						subtaskList.put(subtask.getTaskId(), subtask);
						updateLinkedSubtasks(getEpicLocal(subtask.getEpicId()));
						updateEpicStatus(getEpicLocal(subtask.getEpicId()));
				}
		}

		@Override
		public void removeTask(int id) {
				taskList.remove(id);
				history.remove(id);
		}

		@Override
		public void removeEpic(int id) {
				Epic epic = getEpicLocal(id);
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				for (Subtask subtask : linkedSubtasks) {
						removeSubtask(subtask.getTaskId());
				}
				epicList.remove(id);
				history.remove(id);
		}

		@Override
		public void removeSubtask(int id) {
				Subtask subtask = getSubtask(id);
				subtaskList.remove(id);
				updateEpicStatus(getEpicLocal(subtask.getEpicId()));
				history.remove(id);
		}

		@Override
		public ArrayList<Subtask> getLinkedSubtasks(Epic epic) {
				return epic.getLinkedSubtasks();
		}

		@Override
		public ArrayList<Task> getHistory() {
				return new ArrayList<>(history.getHistory());
		}

		private Epic getEpicLocal(int id) {
				return epicList.get(id);
		}

		private void updateEpicStatus(Epic epic) {
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				if (linkedSubtasks.isEmpty()) {
						epic.setStatus(Status.NEW);
				} else {
						ArrayList<Status> subtaskStatuses = new ArrayList<>();
						for (Subtask subtask : linkedSubtasks) {
								Status status = subtask.getStatus();
								subtaskStatuses.add(status);
						}
						if (!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.DONE)) {
								epic.setStatus(Status.NEW);
						} else if (!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.NEW)) {
								epic.setStatus(Status.DONE);
						} else {
								epic.setStatus(Status.IN_PROGRESS);
						}
				}
		}

		private void updateLinkedSubtasks(Epic epic) {
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				ArrayList<Subtask> newLinkedSubtasks = new ArrayList<>();
				for (Subtask subtask : linkedSubtasks) {
						Subtask linkedSubtask = getSubtask(subtask.getTaskId());
						newLinkedSubtasks.add(linkedSubtask);
				}
				epic.setLinkedSubtasks(newLinkedSubtasks);
		}
}