package managerstypes;

import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;
import taskstypes.Task;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager{
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
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
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
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
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
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
    public void removeSubTask(int id) { //
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    private String toString(Task task) {
        if (task instanceof SubTask) {
            SubTask subtask = (SubTask) task;
            return subtask.getId() + ",SUBTASK," + subtask.getTaskName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getEpicId() + "\n";
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            return epic.getId() + ",EPIC," + epic.getTaskName() + "," + epic.getStatus() + ","
                    + epic.getDescription() + ",\n";
        } else {
            return task.getId() + ",TASK," + task.getTaskName() + "," + task.getStatus() + ","
                    + task.getDescription() + ",\n";
        }
    }

    public void save(){
    }
}
