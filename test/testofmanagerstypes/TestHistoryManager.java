package testofmanagerstypes;

import managerstypes.HistoryManager;
import managerstypes.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskstypes.StatusOfTask;
import taskstypes.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TestHistoryManager {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Задача1", "Описание задачи1", StatusOfTask.NEW, 1,
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 2, 7, 10, 0));
        task2 = new Task("Задача2", "Описание задачи2", StatusOfTask.IN_PROGRESS, 2,
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 2, 7, 11, 0));
        task3 = new Task("Задача3", "Описание задачи3", StatusOfTask.DONE, 3,
                Duration.ofMinutes(60),
                LocalDateTime.of(2025, 2, 7, 12, 0));
    }

    @Test
    void shouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.addToHistory(task1);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    void shouldNotDuplicateInHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    void shouldRemoveFromStartOfHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(task1.getId());
        int a = historyManager.getHistory().size();
        assertEquals(2, a);
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    void shouldRemoveFromMiddle() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(task2.getId());

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    void shouldUpdatePositionIfDuplicate() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task1);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task1, historyManager.getHistory().get(1));
    }

    @Test
    void shouldRemoveFromEnd() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(task3.getId());

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
    }

    @Test
    void shouldHandleRemoveFromEmptyHistory() {
        historyManager.remove(999);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldHandleRemoveNonExistentTask() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);

        historyManager.remove(999);

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void shouldMaintainOrder() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        var history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }
}
