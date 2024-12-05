package managersTypes;

import tasksTypes.Epic;
import tasksTypes.StatusOfTask;
import tasksTypes.SubTask;
import tasksTypes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;


    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskArray = new ArrayList<>(tasks.values());
        return taskArray;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicArray = new ArrayList<>(epics.values());
        return epicArray;
    }


    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> subTaskArray = new ArrayList<>(subtasks.values());
        return subTaskArray;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus(StatusOfTask.NEW);
            epic.getSubTasksIds().clear();
        }
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
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);

    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
            for (int subTaskId : epic.getSubTasksIds()) {
                subtasks.remove(subTaskId);
        }
    }

    @Override
    public void removeSubTask(int id) { //
        SubTask subTask = subtasks.remove(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<SubTask> getSubTasksForEpic(int epicId) {
        ArrayList<SubTask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subTaskId : epic.getSubTasksIds()) {
                if (subtasks.containsKey(subTaskId)) {
                    result.add(subtasks.get(subTaskId));
                }
            }
        }
        return result;
    }


    public void updateEpicStatus(Epic epic) {
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(StatusOfTask.NEW);
            return;
        }

        boolean isNew = true;
        boolean isDone = true;

        for (Integer subTasksId : epic.getSubTasksIds()) {
            SubTask aSubtask = subtasks.get(subTasksId);

            switch (aSubtask.getStatus()) {
                case IN_PROGRESS:
                    isNew = false;
                    isDone = false;
                    break;
                case DONE:
                    isNew = false;
                    break;
                case NEW:
                    isDone = false;
                    break;
            }

            if (!isNew && !isDone) {
                epic.setStatus(StatusOfTask.IN_PROGRESS);
            } else if (isDone) {
                epic.setStatus(StatusOfTask.DONE);
            } else {
                epic.setStatus(StatusOfTask.NEW);
            }

        }
    }

    public int generateId() {
        return nextId++;
    }
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}