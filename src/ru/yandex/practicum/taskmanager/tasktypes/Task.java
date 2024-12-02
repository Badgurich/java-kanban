package ru.yandex.practicum.taskmanager.tasktypes;

import ru.yandex.practicum.taskmanager.enums.Status;

import java.util.Objects;

public class Task {
		protected String name;
		protected String description;
		protected int taskId;
		protected Status status;

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

		@Override
		public String toString() {
				return "ru.yandex.practicum.taskmanager.tasktypes.Task{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", taskId=" + taskId + ", status=" + status + '}';
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
}