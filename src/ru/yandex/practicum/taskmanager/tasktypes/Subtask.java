package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

public class Subtask extends Task {
		private int linkedEpicId;
		public TaskTypes type = TaskTypes.SUBTASK;

		public Subtask(String name, String description, Status status, int linkedEpicId) {
				super(name, description, status);
				this.linkedEpicId = linkedEpicId;
		}

		public Subtask(String name, String description, int taskId, Status status, int linkedEpicId) {
				super(name, description, taskId, status);
				this.linkedEpicId = linkedEpicId;
		}

		public TaskTypes getType() {
				return type;
		}

		public int getEpicId() {
				return linkedEpicId;
		}

		@Override
		public String toString() {
				return taskId + "," + TaskTypes.EPIC + "," + status + "," + description + "," + linkedEpicId + ",";
		}
}