import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {


		HashMap<Integer, Task> taskList = new HashMap<>();
		HashMap<Integer, Epic> epicList = new HashMap<>();
		HashMap<Integer, Subtask> subtaskList = new HashMap<>();
		public static int idCounter = 1;

		public HashMap<Integer, Task> getTaskList() {
				return taskList;
		}

		public HashMap<Integer, Epic> getEpicList() {
				return epicList;
		}

		public HashMap<Integer, Subtask> getSubtaskList() {
				return subtaskList;
		}

		public void removeAllTasks() {
				taskList.clear();
		}

		public void removeAllEpics() {
				epicList.clear();
		}

		public void removeAllSubtasks() {
				subtaskList.clear();
		}

		public Task getTask(int id) {
				return taskList.get(id);
		}

		public Epic getEpic(int id) {
				return epicList.get(id);
		}

		public Subtask getSubtask(int id) {
				return subtaskList.get(id);
		}

		public void addTask(Task task) {
				task.setTaskId(idCounter++);
				task.setStatus(Statuses.NEW);
				taskList.put(task.getTaskId(), task);
		}

		public void addEpic(Epic epic) {
				epic.setTaskId(idCounter++);
				epic.setStatus(Statuses.NEW);
				epicList.put(epic.getTaskId(), epic);
		}

		public void addSubtask(Subtask subtask) {
				if (epicList.containsKey(subtask.getEpicId())) {
						subtask.setTaskId(idCounter++);
						subtask.setStatus(Statuses.NEW);
						subtaskList.put(subtask.getTaskId(), subtask);
						getEpic(subtask.getEpicId()).linkedSubtasks.add(subtask);
						updateEpicStatus(getEpic(subtask.getEpicId()));
				}
		}

		public void updateTask(Task task) {
				if (taskList.containsKey(task.getTaskId())) {
						taskList.put(task.getTaskId(), task);
				}
		}

		public void updateEpic(Epic epic) {
				if (epicList.containsKey(epic.getTaskId())) {
						epicList.put(epic.getTaskId(), epic);
				}
		}

		public void updateSubtask(Subtask subtask) {
				if (subtaskList.containsKey(subtask.getTaskId())) {
						subtaskList.put(subtask.getTaskId(), subtask);
						updateLinkedSubtasks(getEpic(subtask.getEpicId()));
						updateEpicStatus(getEpic(subtask.getEpicId()));
				}
		}

		public void removeTask(int id) {
				taskList.remove(id);
		}

		public void removeEpic(int id) {
				epicList.remove(id);
		}

		public void removeSubtask(int id) {
				Subtask subtask = getSubtask(id);
				subtaskList.remove(id);
				updateEpicStatus(getEpic(subtask.getEpicId()));
		}

		public ArrayList<Subtask> getLinkedSubtasks(Epic epic) {
				return epic.getLinkedSubtasks();
		}

		private void updateEpicStatus(Epic epic) {
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				if (linkedSubtasks.isEmpty()) {
						epic.setStatus(Statuses.NEW);
				} else {
						ArrayList<Statuses> subtaskStatuses = new ArrayList<>();
						for (Subtask subtask : linkedSubtasks) {
								Statuses status = subtask.getStatus();
								subtaskStatuses.add(status);
						}
						if (!subtaskStatuses.contains(Statuses.IN_PROGRESS) && !subtaskStatuses.contains(Statuses.DONE)) {
								epic.setStatus(Statuses.NEW);
						} else if (!subtaskStatuses.contains(Statuses.IN_PROGRESS) && !subtaskStatuses.contains(Statuses.NEW)) {
								epic.setStatus(Statuses.DONE);
						} else {
								epic.setStatus(Statuses.IN_PROGRESS);
						}
				}
		}

		private void updateLinkedSubtasks (Epic epic) {
				ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
				ArrayList<Subtask> newLinkedSubtasks = new ArrayList<>();
				for (Subtask subtask : linkedSubtasks) {
						Subtask linkedSubtask = getSubtask(subtask.getTaskId());
						newLinkedSubtasks.add(linkedSubtask);
				}
				epic.setLinkedSubtasks(newLinkedSubtasks);
		}
}