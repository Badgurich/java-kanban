package ru.yandex.practicum.taskmanager.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTaskManagerTest {

		@Test
		void loadFromFileTest() {
				FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(new File("test/ru/yandex/practicum/taskmanager/resources/test.csv"));
				List<Task> expectedTasks = new ArrayList<>();
				List<Epic> expectedEpics = new ArrayList<>();
				List<Subtask> expectedSubtasks = new ArrayList<>();
				Task task = new Task("Task1", "Description task1", 1, Status.NEW);
				Epic epic = new Epic("Epic2", "Description epic2", 2, Status.DONE);
				Subtask subtask = new Subtask("Sub Task2", "Description sub task3", 3, Status.DONE, epic.getTaskId());
				expectedTasks.add(task);
				expectedEpics.add(epic);
				expectedSubtasks.add(subtask);
				List<Task> tasks = tm.getTaskList();
				List<Epic> epics = tm.getEpicList();
				List<Subtask> subtasks = tm.getSubtaskList();
				assertEquals(expectedTasks, tasks);
				assertEquals(expectedEpics, epics);
				assertEquals(expectedSubtasks, subtasks);
		}

		@Test
		void loadFromEmptyFileTest() {
				FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(new File("test/ru/yandex/practicum/taskmanager/resources/empty.csv"));
				List<Task> tasks = tm.getTaskList();
				List<Epic> epics = tm.getEpicList();
				List<Subtask> subtasks = tm.getSubtaskList();
				assertTrue(tasks.isEmpty());
				assertTrue(epics.isEmpty());
				assertTrue(subtasks.isEmpty());
		}

		@Test
		void loadFromNonExistantFileTest() {
				ManagerSaveException thrown = assertThrows(ManagerSaveException.class, () -> {
						FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(new File("fail.csv"));
				});
				assertEquals("Ошибка загрузки из файла", thrown.getMessage());
		}

		@Test
		void saveTest() throws IOException {
				FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(new File("test/ru/yandex/practicum/taskmanager/resources/empty.csv"));
				List<Task> expectedTasks = new ArrayList<>();
				List<Epic> expectedEpics = new ArrayList<>();
				List<Subtask> expectedSubtasks = new ArrayList<>();
				Task task = new Task("Task1", "Description task1", 1, Status.NEW);
				Epic epic = new Epic("Epic2", "Description epic2", 2, Status.DONE);
				Subtask subtask = new Subtask("Sub Task2", "Description sub task3", 3, Status.DONE, epic.getTaskId());
				tm.addTask(task);
				tm.addEpic(epic);
				tm.addSubtask(subtask);
				expectedTasks.add(task);
				expectedEpics.add(epic);
				expectedSubtasks.add(subtask);
				List<Task> tasks = tm.getTaskList();
				List<Epic> epics = tm.getEpicList();
				List<Subtask> subtasks = tm.getSubtaskList();
				assertEquals(expectedTasks, tasks);
				assertEquals(expectedEpics, epics);
				assertEquals(expectedSubtasks, subtasks);
				new FileWriter("test/ru/yandex/practicum/taskmanager/resources/empty.csv", false).close();
		}
}