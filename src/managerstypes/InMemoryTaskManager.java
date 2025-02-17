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
        checkIntersectionOfTasks(task);
        task.setId(generateId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subtask) {
        checkIntersectionOfTasks(subtask);
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
        Task oldTask = tasks.get(task.getId());
        prioritizedTasks.remove(oldTask);
        checkIntersectionOfTasks(task);
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
        SubTask oldSubTask = subtasks.get(subtask.getId());
        prioritizedTasks.remove(oldSubTask);
        checkIntersectionOfTasks(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.getEpicId()));
        updateEpicTimings(epics.get(subtask.getEpicId()).getId());
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void removeTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        epics.get(id).getSubTasksIds().forEach(subTaskId -> {
            prioritizedTasks.remove(subtasks.get(subTaskId));
            subtasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        });
        epics.remove(id);
        historyManager.remove(id);
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

        long doneCount = epic.getSubTasksIds().stream()
                .map(subtasks::get)
                .filter(subTask -> subTask.getStatus() == StatusOfTask.DONE)
                .count();

        long newCount = epic.getSubTasksIds().stream()
                .map(subtasks::get) //
                .filter(subTask -> subTask.getStatus() == StatusOfTask.NEW) //
                .count();

        if (newCount == epic.getSubTasksIds().size()) {
            epic.setStatus(StatusOfTask.NEW);
        } else if (doneCount == epic.getSubTasksIds().size()) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
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

        List<SubTask> subTasks = getSubTasksForEpic(epicId);

        epic.setDuration(subTasks.stream()
                .map(Task::getDuration)
                .filter(start -> start != null)
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

    private void checkIntersectionOfTasks(Task task) {
        boolean hasIntersection = prioritizedTasks.stream()
                .anyMatch(t -> t.getStartTime().isBefore(task.getEndTime())
                        && task.getStartTime().isBefore(t.getEndTime()));

        if (hasIntersection) {
            throw new IllegalStateException("Задача пересекается по времени с другой задачей!");
        }
    }
}