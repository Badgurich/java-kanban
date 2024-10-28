public class Subtask extends Task {
		int linkedEpicId;

		public Subtask(String name, String description, Statuses status, int linkedEpicId) {
				super(name, description, status);
				this.linkedEpicId = linkedEpicId;
		}

		public Subtask(String name, String description, int taskId, Statuses status, int linkedEpicId) {
				super(name, description, taskId, status);
				this.linkedEpicId = linkedEpicId;
		}

		public int getEpicId() {
				return linkedEpicId;
		}

		@Override
		public String toString() {
				return "Subtask{" +
								"epic=" + linkedEpicId +
								", name='" + name + '\'' +
								", description='" + description + '\'' +
								", taskId=" + taskId +
								", status=" + status +
								'}';
		}
}
