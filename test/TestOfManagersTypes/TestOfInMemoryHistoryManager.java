package TestOfManagersTypes;

import ManagersTypes.HistoryManager;
import ManagersTypes.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import TasksTypes.Epic;
import TasksTypes.Task;
import TasksTypes.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOfInMemoryHistoryManager {


    private HistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void setUp() {
        inMemoryHistoryManager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddAndGetHistory() {
        Task task1 = new Task("Задача1","тема задачи 1");
        Epic epic1 = new Epic("Эпик1", "Описание эпика1");
        SubTask subtask1 = new SubTask("Подзадача1", "Описание подзадачи1", epic1.getId());
        inMemoryHistoryManager.addToHistory(task1);
        inMemoryHistoryManager.addToHistory(epic1);
        inMemoryHistoryManager.addToHistory(subtask1);
        assertEquals(3, inMemoryHistoryManager.getHistory().size());
        assertTrue(inMemoryHistoryManager.getHistory().contains(task1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(epic1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(subtask1));

    }

    @Test
    void shouldRemoveFirstTaskWhenAddNewTaskAndListAreFull() {
        Task task11 = new Task("Задача11", "тема задачи1");
        for (int i = 0; i < 10; i++) {
            inMemoryHistoryManager.addToHistory(new Task("Задача" + i, "тема задачи" + i));
        }
        inMemoryHistoryManager.addToHistory(task11);

        assertEquals(10, inMemoryHistoryManager.getHistory().size());
        assertEquals(task11, inMemoryHistoryManager.getHistory().get(9));

    }
}
