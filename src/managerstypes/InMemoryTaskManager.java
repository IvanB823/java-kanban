package managerstypes;

import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;
import taskstypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected  HashMap<Integer, Task> tasks;
    protected  HashMap<Integer, Epic> epics;
    protected  HashMap<Integer, SubTask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks;
    private int nextId = 1;


    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            prioritizedTasks.remove(epic);
        });
        subtasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subtasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        epics.values().forEach(epic -> {
            epic.setStatus(StatusOfTask.NEW);
            epic.getSubTasksIds().clear();
        });
        subtasks.clear();
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.addToHistory(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.addToHistory(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
            if (subtasks.get(id) != null) {
                historyManager.addToHistory(subtasks.get(id));
            }
        return subtasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTasksIds().add(subtask.getId());
        updateEpicStatus(epic);
        updateEpicTimings(epic.getId());
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        updateEpicTimings(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        updateEpicTimings(epic.getId());
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
    }

    @Override
    public void removeEpic(int id) {
        epics.get(id).getSubTasksIds().forEach(subTaskId -> {
                subtasks.remove(subTaskId);
                historyManager.remove(subTaskId);
                prioritizedTasks.remove(subTaskId);
        });
        epics.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(epics.get(id));
    }

    @Override
    public void removeSubTask(int id) {
        SubTask subTask = subtasks.remove(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksIds().remove(Integer.valueOf(id));
        historyManager.remove(id);
        updateEpicStatus(epic);
        prioritizedTasks.remove(subTask);
        updateEpicTimings(subTask.getEpicId());
    }

    @Override
    public ArrayList<SubTask> getSubTasksForEpic(int epicId) {
        return epics.get(epicId).getSubTasksIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public void updateEpicStatus(Epic epic) {
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(StatusOfTask.NEW);
            return;
        }

        boolean hasInProgress = epic.getSubTasksIds().stream()
                .map(subTaskId -> subtasks.get(subTaskId).getStatus())
                .anyMatch(status -> status == StatusOfTask.IN_PROGRESS);

        boolean allDone = epic.getSubTasksIds().stream()
                .map(subTaskId -> subtasks.get(subTaskId).getStatus())
                .allMatch(status -> status == StatusOfTask.DONE);

        if (hasInProgress) {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        } else if (allDone) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.NEW);
        }
    }
    public void updateEpicTimings(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setDuration(Duration.ZERO);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }

        List<Task> subTasks = getSubTasksForEpic(epicId).stream()
                .map(subtaskId -> subtasks.get(subtaskId))
                .collect(Collectors.toList());

        epic.setDuration(subTasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus));

        epic.setStartTime(subTasks.stream()
                .map(Task::getStartTime)
                .filter(start -> start != null)
                .min(LocalDateTime::compareTo)
                .orElse(null));

        epic.setEndTime(subTasks.stream()
                .map(Task::getEndTime)
                .filter(end -> end != null)
                .max(LocalDateTime::compareTo)
                .orElse(null));
    }

    public int generateId() {
        return nextId++;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}