package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

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

		public Subtask(String name, String description, int taskId, Status status, Duration duration, LocalDateTime startTime, int linkedEpicId) {
				super(name, description, taskId, status, duration, startTime);
				this.linkedEpicId = linkedEpicId;
		}

		@Override
		public TaskTypes getType() {
				return TaskTypes.SUBTASK;
		}

		public int getEpicId() {
				return linkedEpicId;
		}

		@Override
		public String toString() {
				return taskId + "," + TaskTypes.SUBTASK + "," + name + "," + status + "," + description + "," + getDuration().toMinutes() + "," + getStartTime() + "," + linkedEpicId + ",";
		}
}