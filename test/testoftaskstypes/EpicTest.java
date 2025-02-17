package testoftaskstypes;

import managerstypes.Managers;
import managerstypes.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EpicTest {

    private TaskManager manager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        epic = new Epic("Эпик1", "тема эпика 1");
        manager.addEpic(epic);
    }

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик2", "тема эпика 2", 2);
        System.out.println(epic1.getId());
        Epic epic2 = new Epic("Эпик3", "тема эпика 3", 2);
        assertEquals(epic1, epic2, "Ошибка сверки!");
        System.out.println(epic2.getId());
    }
    @Test
    void shouldNotAddEpicHimselfForItsOwnSubtasks() {
        Epic epic1 = new Epic("Эпик2", "тема эпика 2", 2);
        epic1.addSubtask(epic1.getId());
        assertNull(epic1.getSubTasksIds());
    }

    @Test
    void shouldBeNewIfHasNoSubtasks() {
        assertEquals(StatusOfTask.NEW, epic.getStatus());
    }

    @Test
    void shouldBeNewIfAllSubtasksNew() {
        SubTask subTask1 = new SubTask("подЗадача1", "тема подзадачи 1", StatusOfTask.NEW, 2, epic.getId(), null, null);
        SubTask subTask2 = new SubTask("подЗадача2", "тема подзадачи 2", StatusOfTask.NEW, 3, epic.getId(), null, null);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(StatusOfTask.NEW, epic.getStatus());
    }

    @Test
    void shouldBeDoneIfAllSubtasksDone() {
        SubTask subTask1 = new SubTask("Подзадача1", "тема подзадачи 1", StatusOfTask.DONE, 2, epic.getId(), null, null);
        SubTask subTask2 = new SubTask("Подзадача2", "тема подзадачи 2", StatusOfTask.DONE, 3, epic.getId(), null, null);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(StatusOfTask.DONE, epic.getStatus());
    }

    @Test
    void shouldBeInProgressIfSubtasksAreNewAndDone() {
        SubTask subTask1 = new SubTask("Подзадача1", "тема подзадачи 1", StatusOfTask.NEW, 2, epic.getId(), null, null);
        SubTask subTask2 = new SubTask("Подзадача2", "тема подзадачи 2", StatusOfTask.DONE, 3, epic.getId(), null, null);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldBeInProgressIfSubtasksInProgress() {
        SubTask subTask1 = new SubTask("Подзадача1", "тема подзадачи 1", StatusOfTask.IN_PROGRESS, 2, epic.getId(), null, null);
        SubTask subTask2 = new SubTask("Подзадача2", "тема подзадачи 2", StatusOfTask.IN_PROGRESS, 3, epic.getId(), null, null);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(StatusOfTask.IN_PROGRESS, epic.getStatus());
    }

}
