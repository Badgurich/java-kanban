package ru.yandex.practicum.taskmanager.util;

import ru.yandex.practicum.taskmanager.enums.Status;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
		private HashMap<Integer, Task> taskList = new HashMap<>();
		private HashMap<Integer, Epic> epicList = new HashMap<>();
		private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
		public static int idCounter = 1;

		public ArrayList<Task> getTaskList() {
				return new ArrayList<>(taskList.values());
		}

		public ArrayList<Epic> getEpicList() {
				return new ArrayList<>(epicList.values());
		}

		public ArrayList<Subtask> getSubtaskList() {
				return new ArrayList<>(subtaskList.values());
		}

		public void removeAllTasks() {
				taskList.clear();
		}

		public void removeAllEpics() {
				epicList.clear();
				subtaskList.clear();
		}

		public void removeAllSubtasks() {
				subtaskList.clear();
				for (Epic epic : epicList.values()) {
						updateEpicStatus(epic);
				}
		}

		public Task getTask(int id) {
				return taskList.get(id);
		}

		public Epic getEpic(int id) {
				return epicList.get(id);
		}

		public Subtask getSubtask(int id) {
				return subtaskList.get(id);
		}

		public void addTask(Task task) {
				task.setTaskId(idCounter++);
				taskList.put(task.getTaskId(), task);
		}

		public void addEpic(Epic epic) {
				epic.setTaskId(idCounter++);
				epicList.put(epic.getTaskId(), epic);
		}

		public void addSubtask(Subtask subtask) {
				if (epicList.containsKey(subtask.getEpicId())) {
						subtask.setTaskId(idCounter++);
						subtaskList.put(subtask.getTaskId(), subtask);
						getEpic(subtask.getEpicId()).linkedSubtasks.add(subtask);
						updateEpicStatus(getEpic(subtask.getEpicId()));
				}
		}

		public void updateTask(Task task) {
				if (taskList.containsKey(task.getTaskId())) {
						taskList.put(task.getTaskId(), task);
				}
		}

		public void updateEpic(Epic epic) {
				if (epicList.containsKey(epic.getTaskId())) {
						epicList.put(epic.getTaskId(), epic);
				}
		}

		public void updateSubtask(Subtask subtask) {
				if (subtaskList.containsKey(subtask.getTaskId())) {
						subtaskList.put(subtask.getTaskId(), subtask);
						updateLinkedSubtasks(getEpic(subtask.getEpicId()));
						updateEpicStatus(getEpic(subtask.getEpicId()));
				}
		}

		public void removeTask(int id) {
				taskList.remove(id);
		}

		public void removeEpic(int id) {
				Epic epic = getEpic(id);
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				for (Subtask subtask : linkedSubtasks) {
						removeSubtask(subtask.getTaskId());
				}
				epicList.remove(id);
		}

		public void removeSubtask(int id) {
				Subtask subtask = getSubtask(id);
				subtaskList.remove(id);
				updateEpicStatus(getEpic(subtask.getEpicId()));
		}

		public ArrayList<Subtask> getLinkedSubtasks(Epic epic) {
				return epic.getLinkedSubtasks();
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