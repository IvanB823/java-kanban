package managerstypes;

import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;
import taskstypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        System.out.println("Создаём новую задачу!");
        inMemoryTaskManager.addTask(new Task("Задача1", "тема задачи 1", Duration.ofMinutes(35), LocalDateTime.of(2025, 4, 13, 14, 15)));
        inMemoryTaskManager.addTask(new Task("Задача2", "тема задачи 2", Duration.ofMinutes(35), LocalDateTime.of(2025, 5, 13, 14, 15)));
        inMemoryTaskManager.addTask(new Task("Задача3", "тема задачи 3", Duration.ofMinutes(35), LocalDateTime.of(2025, 6, 13, 14, 15)));
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(1);
        System.out.println(inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.addEpic(new Epic("Эпик1", "тема эпика 1"));
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача2", "тема задачи 2", Duration.ofMinutes(35), LocalDateTime.of(2025, 1, 13, 14, 15), 4));
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача3", "тема задачи 3", Duration.ofMinutes(35), LocalDateTime.of(2025, 2, 13, 14, 15), 4));
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача4", "тема задачи 4", Duration.ofMinutes(35), LocalDateTime.of(2025, 3, 13, 14, 15), 4));
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        inMemoryTaskManager.updateTask(new Task("NEW Задача4", "NEW тема задачи 4", StatusOfTask.IN_PROGRESS, 1, Duration.ofMinutes(35), LocalDateTime.of(2025, 7, 13, 14, 15)));
        System.out.println(inMemoryTaskManager.getAllTasks());
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача2", "NEW тема ПОДзадачи 2", StatusOfTask.NEW, 5, inMemoryTaskManager.getSubTask(5).getEpicId(), Duration.ofMinutes(35), LocalDateTime.of(2025, 8, 13, 14, 15)));
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача3", "NEW тема ПОДзадачи 3", StatusOfTask.NEW, 6, inMemoryTaskManager.getSubTask(6).getEpicId(), Duration.ofMinutes(35), LocalDateTime.of(2025, 9, 13, 14, 15)));
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача4", "NEW тема ПОДзадачи 4", StatusOfTask.IN_PROGRESS, 7, inMemoryTaskManager.getSubTask(7).getEpicId(), Duration.ofMinutes(35), LocalDateTime.of(2025, 10, 13, 14, 15)));
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        inMemoryTaskManager.updateEpic(new Epic("Эпик2", "тема эпика 2", 4, inMemoryTaskManager.getEpic(4).getSubTasksIds()));
        System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.removeEpic(4);
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getEpic(3);
        inMemoryTaskManager.getSubTask(4);

        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        inMemoryTaskManager.removeAllTasks();

        if (inMemoryTaskManager.getAllTasks().isEmpty()) {
            System.out.println("ПУСТО");
        }
    }

}
