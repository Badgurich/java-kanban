import java.util.Objects;

public class Task {
		protected String name;
		protected String description;
		protected int taskId;
		protected Statuses status;

		public Task(String name, String description, Statuses status) {
				this.name = name;
				this.description = description;
				this.status = status;
		}

		public Task(String name, String description, int taskId, Statuses status) {
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

		public Statuses getStatus() {
				return status;
		}

		public void setStatus(Statuses status) {
				this.status = status;
		}

		@Override
		public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Task task = (Task) o;
				return getTaskId() == task.getTaskId() && Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription()) && getStatus() == task.getStatus();
		}

		@Override
		public String toString() {
				return "Task{" +
								"name='" + name + '\'' +
								", description='" + description + '\'' +
								", taskId=" + taskId +
								", status=" + status +
								'}';
		}

		@Override
		public int hashCode() {
				return Objects.hash(getName(), getDescription(), getTaskId(), getStatus());
		}
}