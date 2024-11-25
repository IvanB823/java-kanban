public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        System.out.println("Создаём новую задачу!");
        taskManager.addTask(new Task("Задача1", "тема задачи 1"));
        taskManager.addTask(new Task("Задача2", "тема задачи 2"));

        taskManager.addEpic(new Epic("Эпик1", "тема эпика 1"));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        taskManager.addSubTask(new SubTask("подЗадача2", "тема задачи 2", 3));
        taskManager.addSubTask(new SubTask("подЗадача3", "тема задачи 3", 3));
        taskManager.addSubTask(new SubTask("подЗадача4", "тема задачи 4", 3));
        System.out.println(taskManager.getAllSubTasks());
        taskManager.updateTask(new Task("NEW Задача4", "NEW тема задачи 4", StatusOfTask.IN_PROGRESS
                , 1));
        System.out.println(taskManager.getAllTasks());
        taskManager.updateSubTask(new SubTask("NEW Задача2", "NEW тема ПОДзадачи 2", StatusOfTask.NEW, 4, taskManager.getSubTask(4).getEpicId()));
        taskManager.updateSubTask(new SubTask("NEW Задача3", "NEW тема ПОДзадачи 3", StatusOfTask.NEW, 5, taskManager.getSubTask(4).getEpicId()));
        taskManager.updateSubTask(new SubTask("NEW Задача4", "NEW тема ПОДзадачи 4", StatusOfTask.IN_PROGRESS, 6, taskManager.getSubTask(4).getEpicId()));
        System.out.println(taskManager.getAllSubTasks());

        taskManager.updateEpic(new Epic("Эпик2", "тема эпика 2", 3, taskManager.getEpic(3).getSubTasksIds()));
        System.out.println(taskManager.getAllEpics());
        taskManager.removeEpic(3);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        taskManager.removeAllTasks();
    }
}
