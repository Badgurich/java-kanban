import java.util.ArrayList;

public class Epic extends Task {
		ArrayList<Subtask> linkedSubtasks;

		public Epic(String name, String description, Statuses status, ArrayList<Subtask> linkedSubtasks) {
				super(name, description, status);
				this.linkedSubtasks = linkedSubtasks;
		}
		public Epic(String name, String description, int taskId, Statuses status, ArrayList<Subtask> linkedSubtasks) {
				super(name, description, taskId, status);
				this.linkedSubtasks = linkedSubtasks;
		}

		public ArrayList<Subtask> getLinkedSubtasks() {
				return linkedSubtasks;
		}

		public void setLinkedSubtasks(ArrayList<Subtask> linkedSubtasks) {
				this.linkedSubtasks = linkedSubtasks;
		}

		@Override
		public String toString() {
				return "Epic{" +
								"linkedSubtasks=" + linkedSubtasks +
								", name='" + name + '\'' +
								", description='" + description + '\'' +
								", taskId=" + taskId +
								", status=" + status +
								'}';
		}
}
