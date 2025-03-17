package ru.yandex.practicum.taskmanager.managers;

import ru.yandex.practicum.taskmanager.exceptions.LinkedEpicValidationException;
import ru.yandex.practicum.taskmanager.exceptions.TimeValidationException;
import ru.yandex.practicum.taskmanager.tasktypes.Epic;
import ru.yandex.practicum.taskmanager.tasktypes.Subtask;
import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Managers;
import ru.yandex.practicum.taskmanager.util.Status;
import ru.yandex.practicum.taskmanager.util.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> taskList = new HashMap<>();
    private Map<Integer, Epic> epicList = new HashMap<>();
    private Map<Integer, Subtask> subtaskList = new HashMap<>();
    private Set<Task> sortedTaskSet = new TreeSet<>();
    public int idCounter = 1;
    private HistoryManager history = Managers.getDefaultHistory();
    private final ZoneId zone = ZoneId.of("Europe/Berlin");
    private ZoneOffset offset = zone.getRules().getOffset(LocalDateTime.now());

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public void removeAllTasks() {
        taskList.clear();
        sortedTaskSet.removeIf(task -> task.getType() == TaskTypes.TASK);
    }

    @Override
    public void removeAllEpics() {
        epicList.clear();
        subtaskList.clear();
        sortedTaskSet.removeIf(task -> task.getType() == TaskTypes.SUBTASK);
    }

    @Override
    public void removeAllSubtasks() {
        subtaskList.clear();
        epicList.values().forEach(epic -> {
            updateEpicStatus(epic);
            updateEpicDuration(epic);
            updateEpicStartTime(epic);
        });
        sortedTaskSet.removeIf(task -> task.getType() == TaskTypes.SUBTASK);
    }

    @Override
    public Task getTask(int id) {
        history.add(taskList.get(id));
        return taskList.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        history.add(epicList.get(id));
        return epicList.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        history.add(subtaskList.get(id));
        return subtaskList.get(id);
    }

    @Override
    public void addTask(Task task) throws TimeValidationException {
        validateStartTime(task);
        task.setTaskId(idCounter++);
        taskList.put(task.getTaskId(), task);
        if (task.getStartTime() != null) {
            sortedTaskSet.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setTaskId(idCounter++);
        updateEpicDuration(epic);
        epicList.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) throws TimeValidationException, LinkedEpicValidationException {
            validateStartTime(subtask);
            validateLinkedEpic(subtask);
            subtask.setTaskId(idCounter++);
            subtaskList.put(subtask.getTaskId(), subtask);
            getEpicLocal(subtask.getEpicId()).linkedSubtasks.add(subtask);
            updateEpicStatus(getEpicLocal(subtask.getEpicId()));
            updateEpicDuration(getEpicLocal(subtask.getEpicId()));
            updateEpicStartTime(getEpicLocal(subtask.getEpicId()));
            if (subtask.getStartTime() != null) {
                sortedTaskSet.add(subtask);
            }
    }

    @Override
    public void updateTask(Task task) {
        if (taskList.containsKey(task.getTaskId()) && sortedTaskSet.contains(task)) {
            validateStartTime(task);
            taskList.put(task.getTaskId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicList.containsKey(epic.getTaskId())) {
            epicList.put(epic.getTaskId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws TimeValidationException, LinkedEpicValidationException {
            validateStartTime(subtask);
            validateLinkedEpic(subtask);
            subtaskList.put(subtask.getTaskId(), subtask);
            updateLinkedSubtasks(getEpicLocal(subtask.getEpicId()));
            updateEpicStatus(getEpicLocal(subtask.getEpicId()));
            updateEpicDuration(getEpicLocal(subtask.getEpicId()));
            updateEpicStartTime(getEpicLocal(subtask.getEpicId()));
    }

    @Override
    public void removeTask(int id) {
        sortedTaskSet.remove(taskList.remove(id));
        taskList.remove(id);
        history.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = getEpicLocal(id);
        ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
        linkedSubtasks.forEach(subtask -> {
            removeSubtask(subtask.getTaskId());
            sortedTaskSet.remove(subtask);
        });
        epicList.remove(id);
        history.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = getSubtask(id);
        sortedTaskSet.remove(subtaskList.get(id));
        subtaskList.remove(id);
        updateEpicStatus(getEpicLocal(subtask.getEpicId()));
        updateEpicDuration(getEpicLocal(subtask.getEpicId()));
        updateEpicStartTime(getEpicLocal(subtask.getEpicId()));
        history.remove(id);
    }

    @Override
    public List<Subtask> getLinkedSubtasks(Epic epic) {
        return epic.getLinkedSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.getHistory());
    }

    private Epic getEpicLocal(int id) {
        return epicList.get(id);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
        if (linkedSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Status> subtaskStatuses = new ArrayList<>();
            linkedSubtasks.forEach(subtask -> {
                Status status = subtask.getStatus();
                subtaskStatuses.add(status);
            });
            if (!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.DONE)) {
                epic.setStatus(Status.NEW);
            } else if (!subtaskStatuses.contains(Status.IN_PROGRESS) && !subtaskStatuses.contains(Status.NEW)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    private void updateLinkedSubtasks(Epic epic) {
        ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
        ArrayList<Subtask> newLinkedSubtasks = new ArrayList<>();
        linkedSubtasks.forEach(subtask -> {
            Subtask linkedSubtask = getSubtask(subtask.getTaskId());
            newLinkedSubtasks.add(linkedSubtask);
        });
        epic.setLinkedSubtasks(newLinkedSubtasks);
    }

    private void updateEpicDuration(Epic epic) {
        ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
        if (linkedSubtasks.isEmpty()) {
            epic.setDuration(Duration.ofMinutes(0));
        } else {
            Duration epicDuration = Duration.ofMinutes(0);
            for (Subtask subtask : linkedSubtasks) {
                Duration duration = subtask.getDuration();
                epicDuration = epicDuration.plus(duration);
            }
            epic.setDuration(epicDuration);
        }
    }

    private void updateEpicStartTime(Epic epic) {
        ArrayList<Subtask> linkedSubtasks = epic.getLinkedSubtasks();
        if (linkedSubtasks.isEmpty()) {
            epic.setStartTime(null);
        } else {
            LocalDateTime epicStartTime = epic.getStartTime();
            LocalDateTime finalEpicStartTime = epicStartTime;
            Optional<Subtask> optionalSubtask = linkedSubtasks.stream()
                    .filter(st -> finalEpicStartTime == null || st.getStartTime().isBefore(finalEpicStartTime))
                    .findFirst();
            if (optionalSubtask.isPresent()) {
                epicStartTime = optionalSubtask.get().getStartTime();
            }
            epic.setStartTime(epicStartTime);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTaskSet);
    }

    private void validateStartTime(Task task) {
        Optional<Task> optionalTask = getPrioritizedTasks()
                .stream()
                .filter(t -> (task.getStartTime().toEpochSecond(offset) - t.getEndTime().toEpochSecond(offset)) * (t.getStartTime().toEpochSecond(offset) - task.getEndTime().toEpochSecond(offset)) > 0)
                .findFirst();
        if (optionalTask.isPresent()) {
            throw new TimeValidationException("Время добавляемой задачи пересекается с задачей c id=" + optionalTask.get().getTaskId());
        }
    }

    private void validateLinkedEpic(Subtask subtask) {
        int linkedEpicId = subtask.getEpicId();
        Optional<Epic> optionalEpic = getEpicList()
                .stream()
                .filter(e -> e.getTaskId() == linkedEpicId) // для int
                .findFirst();

        if (optionalEpic.isEmpty()) {
            throw new LinkedEpicValidationException("Отсутствует эпик с id=" + linkedEpicId);
        }
    }
}