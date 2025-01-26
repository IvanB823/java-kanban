package testoftaskstypes;

import org.junit.jupiter.api.Test;
import taskstypes.Task;
import taskstypes.StatusOfTask;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    void shouldReturnTrueIfIdsAreEqual() {
        Task task1 = new Task("Задача1", "тема задачи1", StatusOfTask.NEW, 1);
        Task task2 = new Task("Задача2", "тема задачи2", StatusOfTask.NEW, 1);
        assertEquals(task1, task2, "Сошлось успешно!");
    }
}
