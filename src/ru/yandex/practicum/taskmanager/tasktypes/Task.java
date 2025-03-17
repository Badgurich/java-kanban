package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
		protected String name;
		protected String description;
		protected int taskId;
		protected Status status;
		protected Duration duration;
		protected LocalDateTime startTime;

		public Task() {}

		public Task(String name, String description, Status status) {
				this.name = name;
				this.description = description;
				this.status = status;
		}

		public Task(String name, String description, int taskId, Status status) {
				this.name = name;
				this.description = description;
				this.taskId = taskId;
				this.status = status;
		}

		public Task(String name, String description, int taskId, Status status, Duration duration, LocalDateTime startTime) {
				this.name = name;
				this.description = description;
				this.taskId = taskId;
				this.status = status;
				this.duration = duration;
				this.startTime = startTime;
		}

		public TaskTypes getType() {
				return TaskTypes.TASK;
		}

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}

		public String getDescription() {
				return description;
		}

		public void setDescription(String description) {
				this.description = description;
		}

		public int getTaskId() {
				return taskId;
		}

		public void setTaskId(int taskId) {
				this.taskId = taskId;
		}

		public Status getStatus() {
				return status;
		}

		public void setStatus(Status status) {
				this.status = status;
		}

		public Duration getDuration() {
				if (duration != null) {
						return duration;
				} else return Duration.ofMinutes(0);
		}

		public void setDuration(Duration duration) {
				this.duration = duration;
		}

		public LocalDateTime getStartTime() {
				return startTime;
		}

		public void setStartTime(LocalDateTime startTime) {
				this.startTime = startTime;
		}

		public LocalDateTime getEndTime() {
				return startTime.plus(duration);
		}

		@Override
		public String toString() {
				return taskId + "," + TaskTypes.TASK + "," + name + "," + status + "," + description + "," + duration.toMinutes() + "," + startTime + ",";
		}

		@Override
		public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Task task = (Task) o;
				return getTaskId() == task.getTaskId();
		}

		@Override
		public int hashCode() {
				return Objects.hashCode(getTaskId());
		}

		@Override
		public int compareTo(Task o) {
				return this.startTime.compareTo(o.startTime);
		}
}