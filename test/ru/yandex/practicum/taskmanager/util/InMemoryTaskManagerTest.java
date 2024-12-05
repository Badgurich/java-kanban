package ru.yandex.practicum.taskmanager.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
		@Test
		void addAndGetTaskList() {
				TaskManager tm = Managers.getDefault();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				Task task2 = new Task("Задача 2", "Вторая задача", Status.NEW);
				ArrayList<Task> expectedTasks = new ArrayList<>();
				expectedTasks.add(task1);
				expectedTasks.add(task2);
				tm.addTask(task1);
				tm.addTask(task2);
				List<Task> tasks = tm.getTaskList();
				assertEquals(expectedTasks, tasks);
		}

		@Test
		void addAndGetEpicList() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				ArrayList<Epic> expectedEpics = new ArrayList<>();
				expectedEpics.add(epic1);
				expectedEpics.add(epic2);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				List<Epic> epics = tm.getEpicList();
				assertEquals(expectedEpics, epics);
		}

		@Test
		void addAndGetSubtaskList() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask3 = new Subtask("Сабтаск 3", "Третья сабтаска", Status.NEW, epic2.getTaskId());
				ArrayList<Subtask> expectedSubtasks = new ArrayList<>();
				expectedSubtasks.add(subtask1);
				expectedSubtasks.add(subtask2);
				expectedSubtasks.add(subtask3);
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				tm.addSubtask(subtask3);
				List<Subtask> subtasks = tm.getSubtaskList();
				assertEquals(expectedSubtasks, subtasks);
		}

		@Test
		void removeAllTasks() {
				TaskManager tm = Managers.getDefault();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				Task task2 = new Task("Задача 2", "Вторая задача", Status.NEW);
				tm.addTask(task1);
				tm.addTask(task2);
				assertEquals(2, tm.getTaskList().size());
				tm.removeAllTasks();
				assertEquals(0, tm.getTaskList().size());
		}

		@Test
		void removeAllEpics() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				assertEquals(2, tm.getEpicList().size());
				tm.removeAllEpics();
				assertEquals(0, tm.getEpicList().size());
		}

		@Test
		void removeAllSubtasks() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask3 = new Subtask("Сабтаск 3", "Третья сабтаска", Status.NEW, epic2.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				tm.addSubtask(subtask3);
				assertEquals(3, tm.getSubtaskList().size());
				tm.removeAllSubtasks();
				assertEquals(0, tm.getSubtaskList().size());
		}

		@Test
		void getTask() {
				TaskManager tm = Managers.getDefault();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				tm.addTask(task1);
				assertEquals(task1, tm.getTask(1));
		}

		@Test
		void getEpic() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				tm.addEpic(epic1);
				assertEquals(epic1, tm.getEpic(1));
		}

		@Test
		void getSubtaskAndCounterTest() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask3 = new Subtask("Сабтаск 3", "Третья сабтаска", Status.NEW, epic2.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				tm.addSubtask(subtask3);
				assertEquals(subtask3, tm.getSubtask(5));
		}

		@Test
		void updateTask() {
				TaskManager tm = Managers.getDefault();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				Task task2 = new Task("Задача 2", "Вторая задача", 1, Status.NEW);
				tm.addTask(task1);
				tm.updateTask(task2);
				assertEquals(task2, tm.getTask(1));
		}

		@Test
		void updateEpic() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", 1, Status.NEW);
				tm.addEpic(epic1);
				tm.updateEpic(epic2);
				assertEquals(epic2, tm.getEpic(1));
		}

		@Test
		void updateSubtask() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				tm.addEpic(epic1);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", 2, Status.NEW, epic1.getTaskId());
				tm.addSubtask(subtask1);
				tm.updateSubtask(subtask2);
				assertEquals(subtask2, tm.getSubtask(2));

		}

		@Test
		void removeTask() {
				TaskManager tm = Managers.getDefault();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				tm.addTask(task1);
				assertEquals(task1, tm.getTask(1));
				tm.removeTask(1);
				assertEquals(0, tm.getTaskList().size());
		}

		@Test
		void removeEpic() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Задача 1", "Первая задача", Status.NEW);
				tm.addEpic(epic1);
				assertEquals(epic1, tm.getEpic(1));
				tm.removeEpic(1);
				assertEquals(0, tm.getEpicList().size());
		}

		@Test
		void removeSubtask() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				tm.addEpic(epic1);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", 2, Status.NEW, epic1.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				tm.removeSubtask(2);
				assertEquals(1, tm.getSubtaskList().size());
		}

		@Test
		void getLinkedSubtasks() {
				TaskManager tm = Managers.getDefault();
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				tm.addEpic(epic1);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", 2, Status.NEW, epic1.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				ArrayList<Subtask> expectedSubtasks = new ArrayList<>();
				expectedSubtasks.add(subtask1);
				expectedSubtasks.add(subtask2);
				assertEquals(expectedSubtasks, tm.getLinkedSubtasks(epic1));
		}

		@Test
		void getHistoryTest() {
				TaskManager tm = Managers.getDefault();
				ArrayList<Task> expectedHistory = new ArrayList<>();
				Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
				tm.addTask(task1);
				Epic epic1 = new Epic("Эпик 1", "Первый эпик с двумя сабтасками", Status.NEW);
				Epic epic2 = new Epic("Эпик 2", "Второй эпик с одной сабтаской", Status.NEW);
				tm.addEpic(epic1);
				tm.addEpic(epic2);
				Subtask subtask1 = new Subtask("Сабтаск 1", "Первая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask2 = new Subtask("Сабтаск 2", "Вторая сабтаска", Status.NEW, epic1.getTaskId());
				Subtask subtask3 = new Subtask("Сабтаск 3", "Третья сабтаска", Status.NEW, epic2.getTaskId());
				tm.addSubtask(subtask1);
				tm.addSubtask(subtask2);
				tm.addSubtask(subtask3);
				tm.getTask(1);
				expectedHistory.add(task1);
				tm.getEpic(3);
				expectedHistory.add(epic2);
				tm.getEpic(2);
				expectedHistory.add(epic1);
				tm.getSubtask(4);
				expectedHistory.add(subtask1);
				tm.getSubtask(6);
				expectedHistory.add(subtask3);
				tm.getSubtask(5);
				expectedHistory.add(subtask2);
				tm.getSubtask(5);
				tm.getSubtask(5);
				tm.getSubtask(5);
				tm.getSubtask(5);
				tm.getSubtask(5);
				tm.getTask(1);
				expectedHistory.add(task1);
				expectedHistory.removeFirst();
				assertEquals(expectedHistory, tm.getHistory());

		}
}