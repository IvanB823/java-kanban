package testOfManagersTypes;
import managersTypes.Managers;
import tasksTypes.StatusOfTask;
import tasksTypes.Epic;
import tasksTypes.SubTask;
import tasksTypes.Task;
import managersTypes.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestOfInMemoryTaskManager {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    @BeforeEach
    void staterTasks() {
        taskManager = Managers.getDefault();
        task1 = new Task("Задача1", "Описание задачи1");
        task2 = new Task("Задача2", "Описание задачи2");
        epic1 = new Epic("Эпик1", "Описание эпика1");
        epic2 = new Epic("Эпик2", "Сегодня эпика2");

    }

    @Test
    void shouldAddAndGetNewTasks() {
        taskManager.addTask(task1);
        Task task = taskManager.getTask(task1.getId());
        assertNotNull(task);
        assertEquals(task1, task);
    }

    @Test
    void shouldGetAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void shouldUpdateTaskToNewTask() {
        taskManager.addTask(task1);
        Task updateTask1 = new Task("Задача1", "Описание задачи1",
                StatusOfTask.IN_PROGRESS, task1.getId());
        taskManager.updateTask(updateTask1);
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Задача1", tasks.getFirst().getTaskName());

    }

    @Test
    void shouldRemoveAllTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void shouldRemoveTaskById() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.removeTask(task1.getId());
        List<Task> tasks = taskManager.getAllTasks();
        assertFalse(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void shouldAddAndGetNewEpics() {
        taskManager.addEpic(epic1);
        Epic epic = taskManager.getEpic(epic1.getId());
        assertNotNull(epic);
        assertEquals(epic1, epic);


    }

    @Test
    void shouldGetAllEpics() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void shouldUpdateEpicToNewerEpic() {
        taskManager.addEpic(epic1);
        Epic updateEpic1 = new Epic("эпик1", "описание эпика1", epic1.getId(),
                epic1.getSubTasksIds());
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        updateEpic1.addSubtask(epic1.getId());
        taskManager.updateEpic(updateEpic1);
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics);
        assertEquals(1, epics.size());
        assertEquals("описание эпика1", epics.getFirst().getDescription());

    }

    @Test
    void shouldRemoveAllEpicsAndShouldRemoveAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        SubTask subtask2 = new SubTask("Сабтаск2", "описание сабтаск2", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.removeAllEpics();
        List<Epic> epics = taskManager.getAllEpics();
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        assertTrue(epics.isEmpty());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void shouldRemoveEpicByIdAndShouldRemoveAllSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        SubTask subtask2 = new SubTask("Сабтаск2", "описание сабтаск2", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.removeEpic(epic1.getId());
        List<Epic> epics = taskManager.getAllEpics();
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        assertFalse(epics.contains(epic1));
        assertTrue(subtasks.isEmpty());
    }

    @Test
    void shouldAddAndGetNewSubtasks() {
        taskManager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        SubTask subtask = taskManager.getSubTask(subtask1.getId());
        assertNotNull(subtask1);
        assertEquals(subtask1, subtask);
    }

    @Test
    void shouldGetAllSubtasks() {
        taskManager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        SubTask subtask2 = new SubTask("Сабтаск2", "описание сабтаск2", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void shouldUpdateSubtaskToNewSubtaskAndShouldChangeEpicStatus() {
        taskManager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        SubTask subtask2 = new SubTask("Сабтаск2", "описание сабтаск2", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        StatusOfTask epic1Status = epic1.getStatus();
        SubTask subtask3 = new SubTask("Сабтаск3", "описание сабтаск3", StatusOfTask.DONE,
                subtask2.getId(), epic1.getId());
        taskManager.updateSubTask(subtask3);
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        StatusOfTask actualEpicStatus = epic1.getStatus();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask3));
        assertNotEquals(epic1Status, actualEpicStatus);
    }

    @Test
    void shouldRemoveSubtaskByIdAndShouldChangeEpicStatus() {
        taskManager.addEpic(epic1);
        SubTask subtask1 = new SubTask("Сабтаск1", "описание сабтаск1", StatusOfTask.NEW,
                epic1.getId(), epic1.getId());
        SubTask subtask2 = new SubTask("Сабтаск2", "описание сабтаск2", StatusOfTask.DONE,
                epic1.getId(), epic1.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        StatusOfTask epic1Status = epic1.getStatus();
        taskManager.removeSubTask(subtask1.getId());
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        StatusOfTask actualEpicStatus = epic1.getStatus();
        assertNotNull(subtasks);
        assertEquals(1, subtasks.size());
        assertTrue(subtasks.contains(subtask2));
        assertNotEquals(epic1Status, actualEpicStatus);

    }
}
