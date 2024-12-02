package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.enums.Status;

import java.util.ArrayList;

public class Epic extends Task {
		public ArrayList<Subtask> linkedSubtasks = new ArrayList<>();

		public Epic(String name, String description, Status status) {
				super(name, description, status);
		}

		public Epic(String name, String description, int taskId, Status status) {
				super(name, description, taskId, status);
		}

		public ArrayList<Subtask> getLinkedSubtasks() {
				return linkedSubtasks;
		}

		public void setLinkedSubtasks(ArrayList<Subtask> linkedSubtasks) {
				this.linkedSubtasks = linkedSubtasks;
		}

		@Override
		public String toString() {
				return "ru.yandex.practicum.taskmanager.tasktypes.Epic{" + "linkedSubtasks=" + linkedSubtasks + ", name='" + name + '\'' + ", description='" + description + '\'' + ", taskId=" + taskId + ", status=" + status + '}';
		}
}