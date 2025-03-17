package ru.yandex.practicum.taskmanager.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.taskmanager.exceptions.LineParsingException;
import ru.yandex.practicum.taskmanager.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskmanager.managers.FileBackedTaskManager;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
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
        Task task = new Task("Task1", "Description task1", 1, Status.NEW, Duration.ofMinutes(20), LocalDateTime.parse("2000-01-01T01:00"));
        Epic epic = new Epic("Epic2", "Description epic2", 2);
        epic.setDuration(Duration.ofMinutes(20));
        epic.setStartTime(LocalDateTime.parse("2000-01-01T01:21"));
        Subtask subtask = new Subtask("Sub Task2", "Description sub task3", 3, Status.DONE, Duration.ofMinutes(20), LocalDateTime.parse("2000-01-01T01:21"), epic.getTaskId());
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
    void loadFromEmptyFileTest() throws IOException {
        File temp = File.createTempFile("empty", "csv");
        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(temp);
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
        File temp = File.createTempFile("empty", "csv");
        FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(temp);
        List<Task> expectedTasks = new ArrayList<>();
        List<Epic> expectedEpics = new ArrayList<>();
        List<Subtask> expectedSubtasks = new ArrayList<>();
        Task task = new Task("Task1", "Description task1", 1, Status.NEW);
        Epic epic = new Epic("Epic2", "Description epic2", 2);
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
    }

    @Test
    void subtuskWithoutEpicId() {
        LineParsingException thrown = assertThrows(LineParsingException.class, () -> {
            FileBackedTaskManager tm = FileBackedTaskManager.loadFromFile(new File("test/ru/yandex/practicum/taskmanager/resources/test-LineParsingException.csv"));
        });
        assertEquals("У Subtask отсутствует epicId", thrown.getMessage());
    }
}