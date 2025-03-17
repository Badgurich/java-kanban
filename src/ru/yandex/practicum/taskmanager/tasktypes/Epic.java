package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.util.ArrayList;

public class Epic extends Task {

		public ArrayList<Subtask> linkedSubtasks = new ArrayList<>();

		public Epic() {
		}

		public Epic(String name, String description) {
				super(name, description, Status.NEW);
		}

		public Epic(String name, String description, int taskId) {
				super(name, description, taskId, Status.NEW);
		}

		@Override
		public TaskTypes getType() {
				return TaskTypes.EPIC;
		}

		public ArrayList<Subtask> getLinkedSubtasks() {
				return linkedSubtasks;
		}

		public void setLinkedSubtasks(ArrayList<Subtask> linkedSubtasks) {
				this.linkedSubtasks = linkedSubtasks;
		}

		@Override
		public String toString() {
				return taskId + "," + TaskTypes.EPIC + "," + name + "," + status + "," + description + ","
								+ getDuration().toMinutes() + "," + getStartTime() + "," + getLinkedSubtasks() + ",";
		}
}