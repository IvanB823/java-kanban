package ManagersTypes;
import java.util.List;
import TasksTypes.Epic;
import TasksTypes.SubTask;
import TasksTypes.Task;

import java.util.ArrayList;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubTask(int id);

    ArrayList<SubTask> getSubTasksForEpic(int epicId);


}


