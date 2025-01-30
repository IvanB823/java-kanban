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

    public void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n"); // Заголовок CSV
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
            }
            for (SubTask subtask : getAllSubTasks()) {
                writer.write(toString(subtask));
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("ОШИБКА: данные не сохранены в файл",exception);
        }
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

    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];

        switch (type) {
            case "TASK":
                return new Task(parts[2], parts[4], StatusOfTask.valueOf(parts[3]), id);
            case "SUBTASK":
                int epicId = Integer.parseInt(parts[5]);
                return new SubTask(parts[2], parts[4], StatusOfTask.valueOf(parts[3]), id, epicId);
            case "EPIC":
                return new Epic(parts[2], parts[4], id, null); // Здесь можно добавить логику для подзадач, если нужно
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
