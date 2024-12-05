package managersTypes;

import tasksTypes.Epic;
import tasksTypes.SubTask;
import tasksTypes.Task;

import java.util.ArrayList;

public interface TaskManager {


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


