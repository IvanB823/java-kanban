package managersTypes;

import tasksTypes.Epic;
import tasksTypes.StatusOfTask;
import tasksTypes.SubTask;
import tasksTypes.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        System.out.println("Создаём новую задачу!");
        inMemoryTaskManager.addTask(new Task("Задача1", "тема задачи 1"));
        inMemoryTaskManager.addTask(new Task("Задача2", "тема задачи 2"));
        inMemoryTaskManager.addTask(new Task("Задача3", "тема задачи 3"));
        inMemoryTaskManager.getTask(1);
        inMemoryTaskManager.getTask(2);
        inMemoryTaskManager.getTask(3);
        inMemoryTaskManager.getTask(1);
        System.out.println(inMemoryTaskManager.getHistory().size());

        inMemoryTaskManager.addEpic(new Epic("Эпик1", "тема эпика 1"));
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача2", "тема задачи 2", 4));
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача3", "тема задачи 3", 4));
        inMemoryTaskManager.addSubTask(new SubTask("подЗадача4", "тема задачи 4", 4));
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        inMemoryTaskManager.updateTask(new Task("NEW Задача4", "NEW тема задачи 4", StatusOfTask.IN_PROGRESS
                , 1));
        System.out.println(inMemoryTaskManager.getAllTasks());
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача2", "NEW тема ПОДзадачи 2", StatusOfTask.NEW, 4, inMemoryTaskManager.getSubTask(5).getEpicId()));
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача3", "NEW тема ПОДзадачи 3", StatusOfTask.NEW, 4, inMemoryTaskManager.getSubTask(6).getEpicId()));
        inMemoryTaskManager.updateSubTask(new SubTask("NEW Задача4", "NEW тема ПОДзадачи 4", StatusOfTask.IN_PROGRESS, 4, inMemoryTaskManager.getSubTask(7).getEpicId()));
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
