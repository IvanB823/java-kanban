import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subtasks;
    private int nextId = 1;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskArray = new ArrayList<>(tasks.values());
        return taskArray;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicArray = new ArrayList<>(epics.values());
        return epicArray;
    }

  
    public  ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> subTaskArray = new ArrayList<>(subtasks.values());
        return subTaskArray;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubTasks() {
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.setStatus(StatusOfTask.NEW);
            epic.getSubTasksIds().clear();
        }
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

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTasksIds().add(subtask.getId());
        updateEpicStatus(epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);

    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    public void updateSubTask(SubTask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
            for (int subTaskId : epic.getSubTasksIds()) {
                subtasks.remove(subTaskId);
        }
    }

    public void removeSubTask(int id) { //
        SubTask subTask = subtasks.remove(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic);
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

    private void updateEpicStatus(Epic epic) {
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

    private int generateId () {
        return nextId++;
    }
}