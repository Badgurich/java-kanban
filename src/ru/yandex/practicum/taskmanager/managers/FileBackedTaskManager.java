package ru.yandex.practicum.taskmanager.managers;

import ru.yandex.practicum.taskmanager.exceptions.LineParsingException;
import ru.yandex.practicum.taskmanager.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.taskmanager.util.TaskTypes.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File saveFile;

    public FileBackedTaskManager() {
        super();
    }

    public FileBackedTaskManager(File saveFile) {
        super();
        this.saveFile = saveFile;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    private String toString(Task task) {
        if (task.getType().equals(SUBTASK)) {
            Subtask subtask = (Subtask) task;
            return subtask.getTaskId() + ","
                    + subtask.getType() + ","
                    + subtask.getName() + ","
                    + subtask.getStatus() + ","
                    + subtask.getDescription() + ","
                    + subtask.getDuration().toMinutes() + ","
                    + subtask.getStartTime() + ","
                    + subtask.getEpicId();
        } else {
            return task.getTaskId() + ","
                    + task.getType() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ","
                    + task.getDuration().toMinutes() + ","
                    + task.getStartTime();
        }
    }

    private void save() {
        try (Writer w = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
            w.append("id,type,name,status,description,duration,start time,epic" + "\n");
            for (Task task : getTaskList()) {
                w.append(toString(task)).append("\n");
            }
            for (Epic epic : getEpicList()) {
                w.append(toString(epic)).append("\n");
            }
            for (Subtask subtask : getSubtaskList()) {
                w.append(toString(subtask)).append("\n");
            }
            Files.writeString(saveFile.toPath(), w.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    private static Task fromString(String value) {
        String[] field = value.split(",");
        int id = Integer.parseInt(field[0]);
        TaskTypes type = valueOf(field[1]);
        String name = field[2];
        Status status = Status.valueOf(field[3]);
        String description = field[4];
        Duration duration = Duration.ofMinutes(Integer.parseInt(field[5]));
        LocalDateTime startTime = LocalDateTime.parse(field[6]);
        int epicId = -1;
        if (type.equals(SUBTASK)) {
            epicId = Integer.parseInt(field[7]);
        }
        Task task = null;
        switch (type) {
            case TASK:
                task = new Task(name, description, id, status, duration, startTime);
                break;
            case EPIC:
                task = new Epic(name, description, id);
                break;
            case SUBTASK:
                if (epicId != -1 && epicId != 0) {
                    task = new Subtask(name, description, id, status, duration, startTime, epicId);
                } else if (epicId == -1) {
                    throw new LineParsingException("У Subtask отсутствует epicId");
                }
                break;
            default:
                throw new LineParsingException("Тип задачи не существует");
        }
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager tm = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                if (task.getType().equals(EPIC)) {
                    tm.addEpic((Epic) task);
                } else if (task.getType().equals(SUBTASK)) {
                    tm.addSubtask((Subtask) task);
                } else {
                    tm.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        } catch (IllegalArgumentException e) {
            try (FileWriter fw = new FileWriter(file, true)) {
                fw.write("id,type,name,status,description,epic" + "\n");
            } catch (IOException ex) {
                throw new ManagerSaveException("Ошибка загрузки из файла", ex);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new LineParsingException("У Subtask отсутствует epicId");
        }
        return tm;
    }
}
