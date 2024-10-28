import java.util.ArrayList;

static void main(String[] args) {
		TaskManager tm = new TaskManager();

		Task task1 = new Task("Задача 1", "Первая задача", Statuses.NEW);
		Task task2 = new Task("Задача 2", "Вторая задача", Statuses.NEW);
		tm.addTask(task1);
		tm.addTask(task2);

		Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Statuses.NEW, new ArrayList<>());
		Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Statuses.NEW, new ArrayList<>());
		tm.addEpic(epic1);
		tm.addEpic(epic2);

		Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Statuses.NEW, epic1.getTaskId());
		Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", Statuses.NEW, epic1.getTaskId());
		Subtask subtask3 = new Subtask("Сабтаск 3", "Третья сабтаска", Statuses.NEW, epic2.getTaskId());
		tm.addSubtask(subtask1);
		tm.addSubtask(subtask2);
		tm.addSubtask(subtask3);

		System.out.println(tm.getTaskList());
		System.out.println(tm.getEpicList());
		System.out.println(tm.getSubtaskList());

		tm.updateTask(new Task("Задача 1", "Первая задача", task1.getTaskId(), Statuses.IN_PROGRESS));
		tm.updateSubtask(new Subtask("Сабтаск 1", "Первая сабтаска", subtask1.getTaskId(), Statuses.DONE, epic1.getTaskId()));
		tm.updateSubtask(new Subtask("Сабтаск 3", "Третья сабтаска", subtask3.getTaskId() ,Statuses.DONE, epic2.getTaskId()));

		System.out.println("Статус Таск 1: " + tm.getTask(task1.getTaskId()).getStatus());
		System.out.println("Статус Сабтаск 1: " + tm.getSubtask(subtask1.getTaskId()).getStatus());
		System.out.println("Статус Эпик 1: " + tm.getEpic(epic1.getTaskId()).getStatus());
		System.out.println("Статус Эпик 2: " + tm.getEpic(epic2.getTaskId()).getStatus());

		tm.removeTask(task1.getTaskId());
		tm.removeEpic(epic1.getTaskId()); //Надо ли удалять связанные сабтаски?

		System.out.println(tm.getTaskList());
		System.out.println(tm.getEpicList());
		System.out.println(tm.getSubtaskList());
}
