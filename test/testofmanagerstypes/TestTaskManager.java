package testofmanagerstypes;

import managerstypes.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.StatusOfTask;
import taskstypes.Task;
import taskstypes.Epic;
import taskstypes.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

abstract class TestTaskManager<T extends TaskManager> {
    protected T manager;

    abstract T createManager();

    @BeforeEach
    void setUp() {
        manager = createManager();
    }

    @Test
    public void shouldCreateTask() {
        Task task = new Task("Задача1", "Описание Задачи1", StatusOfTask.NEW, 1,
                Duration.ofMinutes(60),
                LocalDateTime.now());
        manager.addTask(task);
        Task addedNewTask = manager.getTask(task.getId());

        assertNotNull(addedNewTask);
        assertEquals(task.getTaskName(), addedNewTask.getTaskName());
        assertEquals(task.getDescription(), addedNewTask.getDescription());
        assertEquals(task.getStatus(), addedNewTask.getStatus());
    }

    @Test
    public void shouldCreateEpicWithCorrectStatus() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1", 2, new ArrayList<>());
        manager.addEpic(epic);
        Epic addedNewEpic = manager.getEpic(epic.getId());

        assertEquals(StatusOfTask.NEW, addedNewEpic.getStatus());

        SubTask subtask1 = new SubTask("Подзадача1", "Описание Подзадачи1", StatusOfTask.IN_PROGRESS, 3,
                addedNewEpic.getId(),
                Duration.ofMinutes(30),
                LocalDateTime.now());
        SubTask subtask2 = new SubTask("Подзадача2", "Описание Подзадачи2", StatusOfTask.DONE, 4,
                addedNewEpic.getId(),
                Duration.ofMinutes(30),
                LocalDateTime.now().plusHours(1));
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        assertEquals(StatusOfTask.IN_PROGRESS, manager.getEpic(addedNewEpic.getId()).getStatus());
    }

    @Test
    public void shouldPreventTimeOverlap() {
        LocalDateTime startTime = LocalDateTime.now();

        Task task1 = new Task("Задача1", "Описание Задачи1",
                Duration.ofMinutes(60),
                startTime);
        manager.addTask(task1);

        Task task2 = new Task("Задача2", "Описание Задачи2",
                Duration.ofMinutes(60),
                startTime.plusMinutes(30));

        assertThrows(IllegalStateException.class, () -> manager.addTask(task2));
    }

    @Test
    public void shouldManageSubtaskEpicRelation() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1", 7, new ArrayList<>());
        manager.addEpic(epic);
        Epic addedNewEpic = manager.getEpic(epic.getId());

        SubTask subtask = new SubTask("Подзадача1", "Описание Подзадачи1", StatusOfTask.NEW, 8, addedNewEpic.getId(),
                Duration.ofMinutes(60),
                LocalDateTime.now());
        manager.addSubTask(subtask);
        SubTask addedNewSubtask = manager.getSubTask(subtask.getId());

        assertEquals(addedNewEpic.getId(), addedNewSubtask.getEpicId());
        assertTrue(manager.getEpic(addedNewEpic.getId()).getSubTasksIds().contains(addedNewSubtask.getId()));
    }

    @Test
    public void shouldUpdateEpicStatusBasedOnSubtasks() {
        Epic epic = new Epic("Эпик1", "Описание Эпика1", 9, new ArrayList<>());
        manager.addEpic(epic);
        Epic addedNewEpic = manager.getEpic(epic.getId());

        assertEquals(StatusOfTask.NEW, addedNewEpic.getStatus());

        SubTask subtask = new SubTask("Подзадача1", "Описание Эпика1", StatusOfTask.DONE, 10, addedNewEpic.getId(),
                Duration.ofMinutes(30),
                LocalDateTime.now());
        manager.addSubTask(subtask);

        assertEquals(StatusOfTask.DONE, manager.getEpic(addedNewEpic.getId()).getStatus());
    }
}