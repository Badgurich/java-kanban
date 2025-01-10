package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.util.ArrayList;

public class Epic extends Task {
		private final TaskTypes type = TaskTypes.EPIC;

		public ArrayList<Subtask> linkedSubtasks = new ArrayList<>();

		public Epic(String name, String description, Status status) {
				super(name, description, status);
		}

		public Epic(String name, String description, int taskId, Status status) {
				super(name, description, taskId, status);
		}

		public TaskTypes getType() {
				return type;
		}

		public ArrayList<Subtask> getLinkedSubtasks() {
				return linkedSubtasks;
		}

		public void setLinkedSubtasks(ArrayList<Subtask> linkedSubtasks) {
				this.linkedSubtasks = linkedSubtasks;
		}

		@Override
		public String toString() {
				return taskId + "," + TaskTypes.EPIC + "," + status + "," + description + ",";
		}
}