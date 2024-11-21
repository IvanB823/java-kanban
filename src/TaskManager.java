import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subtasks;
    private int nextId;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        nextId = 1;
    }

    public int generateId() {
        return nextId++;
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subtasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public void removeAllSubTasks() {
        subtasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subtasks.get(id);
    }

    public void add(Task task) {
        task.id = generateId();
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epic.id = generateId();
        epics.put(epic.id, epic);
    }

    public void add(SubTask subtask) {
        subtask.id = generateId();
        subtasks.put(subtask.id, subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubTaskId(subtask.id);
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.id, task);
    }

    public void update(Epic epic) {
        epics.put(epic.id, epic);
    }

    public void update(SubTask subtask) {
        subtasks.put(subtask.id, subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStatus(StatusOfTask.DONE);
            return;
        }

        boolean hasInProgress = false;
        boolean hasDone = true;

        for (Integer subTasksId : epic.getSubTasksIds()) {
            SubTask aSubtask = subtasks.get(subTasksId);
            if (aSubtask == null) continue;

            switch (aSubtask.getStatus()) {
                case IN_PROGRESS:
                    hasInProgress = true;
                    hasDone = false;
                    break;
                case DONE:
                    break;
                case NEW:
                    hasDone = false;
                    break;
            }
        }

        if (hasInProgress) {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        } else if (hasDone) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.NEW);
        }
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subTaskId : epic.getSubTasksIds()) {
                subtasks.remove(subTaskId);
            }
        }
    }
    public void removeSubTask(int id) {
        SubTask subTask = subtasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTasksIds().remove(Integer.valueOf(id));
            }
        }
    }

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
}
