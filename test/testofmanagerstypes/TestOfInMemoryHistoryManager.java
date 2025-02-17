package testofmanagerstypes;

import managerstypes.HistoryManager;
import managerstypes.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.Epic;
import taskstypes.StatusOfTask;
import taskstypes.Task;
import taskstypes.SubTask;

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
        Task task1 = new Task("Задача1","тема задачи 1", StatusOfTask.NEW, 0);
        Epic epic1 = new Epic("Эпик1", "Описание эпика1", 1);
        SubTask subtask1 = new SubTask("Подзадача1", "Описание подзадачи1", StatusOfTask.NEW, 2, epic1.getId());
        inMemoryHistoryManager.addToHistory(task1);
        inMemoryHistoryManager.addToHistory(epic1);
        inMemoryHistoryManager.addToHistory(subtask1);
        assertEquals(3, inMemoryHistoryManager.getHistory().size());
        assertTrue(inMemoryHistoryManager.getHistory().contains(task1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(epic1));
        assertTrue(inMemoryHistoryManager.getHistory().contains(subtask1));

    }
}
