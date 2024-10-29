package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.enums.Status;

public class Subtask extends Task {
		private int linkedEpicId;

		public Subtask(String name, String description, Status status, int linkedEpicId) {
				super(name, description, status);
				this.linkedEpicId = linkedEpicId;
		}

		public Subtask(String name, String description, int taskId, Status status, int linkedEpicId) {
				super(name, description, taskId, status);
				this.linkedEpicId = linkedEpicId;
		}

		public int getEpicId() {
				return linkedEpicId;
		}

		@Override
		public String toString() {
				return "ru.yandex.practicum.taskmanager.tasktypes.Subtask{" +
								"epic=" + linkedEpicId +
								", name='" + name + '\'' +
								", description='" + description + '\'' +
								", taskId=" + taskId +
								", status=" + status +
								'}';
		}
}
