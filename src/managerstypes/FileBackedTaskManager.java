package managerstypes;

import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;
import taskstypes.Task;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException { // тестовый мейн
        File file = File.createTempFile("file", ".csv", null);
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        Task task = new Task("Задача 1", "тема задачи 1",
                Duration.ofMinutes(55), LocalDateTime.of(2025, 1, 2, 3, 4));
        fileBackedTaskManager.addTask(task);
        Task task2 = new Task("Задача 2", "тема задачи 2",
                Duration.ofMinutes(10), LocalDateTime.of(2025, 2, 6, 7, 8));
        fileBackedTaskManager.addTask(task2);
        Task task3 = new Task("Задача 3", "тема задачи 3",
                Duration.ofMinutes(15), LocalDateTime.of(2025, 3, 10, 11, 12));
        fileBackedTaskManager.addTask(task3);

        Epic epic = new Epic("Эпик 1", "тема эпика 1");
        fileBackedTaskManager.addEpic(epic);
        Epic epic2 = new Epic("Эпик 2", "тема эпика 2");
        fileBackedTaskManager.addEpic(epic2);

        SubTask subtask = new SubTask("подЗадача1", "тема задачи 1",
                Duration.ofMinutes(35), LocalDateTime.of(2025, 4, 13, 14, 15), epic.getId());
        fileBackedTaskManager.addSubTask(subtask);
        SubTask subtask2 = new SubTask("подЗадача2", "тема задачи 2",
                Duration.ofMinutes(25), LocalDateTime.of(2025, 5, 16, 17, 18), epic.getId());
        fileBackedTaskManager.addSubTask(subtask2);

        FileBackedTaskManager loadedFileBackedTaskManager = loadFromFile(file);

        System.out.println("Количество задач: " + loadedFileBackedTaskManager.getAllTasks().size());
        System.out.println("Количество эпиков: " + loadedFileBackedTaskManager.getAllEpics().size());
        System.out.println("Количество подзадач: " + loadedFileBackedTaskManager.getAllSubTasks().size());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Task task = manager.fromString(line);

                if (task.getClass() == Task.class) {
                    manager.tasks.put(task.getId(), task);
                }

                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                }

                if (task instanceof SubTask) {
                    manager.subtasks.put(task.getId(), (SubTask) task);
                }

            }
        } catch (IOException exception) {
            throw new RuntimeException("ОШИБКА: данные не сохранены из файла", exception);
        }
        return manager;
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

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
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
            throw new ManagerSaveException("ОШИБКА: данные не сохранены в файл", exception);
        }
    }

    private String toString(Task task) {
        String taskStartTime = "null";
        String taskDuration = "null";

        if (task.getStartTime() != null) {
            taskStartTime = task.getStartTime().toString();
        }

        if (task.getStartTime() != null) {
            taskDuration = task.getDuration().toString();
        }

        if (task instanceof SubTask) {
            SubTask subtask = (SubTask) task;
            return subtask.getId() + ",SUBTASK," + subtask.getTaskName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + taskDuration + "," + taskStartTime + "," + subtask.getEpicId()
                    + "\n";
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            return epic.getId() + ",EPIC," + epic.getTaskName() + "," + epic.getStatus() + ","
                    + epic.getDescription() + ",\n";
        } else {
            return task.getId() + ",TASK," + task.getTaskName() + "," + task.getStatus() + ","
                    + task.getDescription() + "," + taskDuration + "," + taskStartTime + ",\n";
        }
    }

    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];

        switch (type) {
            case "TASK":
                return new Task(parts[2], parts[4], StatusOfTask.valueOf(parts[3]), id,
                        (Duration.parse(parts[5])),
                        !parts[6].isBlank() ? LocalDateTime.parse(parts[6]) : null);
            case "SUBTASK":
                int epicId = Integer.parseInt(parts[7]);
                return new SubTask(parts[2], parts[4], StatusOfTask.valueOf(parts[3]), id,
                        Duration.parse(parts[5]),
                        !parts[6].isBlank() ? LocalDateTime.parse(parts[6]) : null, epicId);
            case "EPIC":
                return new Epic(parts[2], parts[4], id, null);
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
