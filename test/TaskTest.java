import org.junit.jupiter.api.Test;
import tasksTypes.Task;
import tasksTypes.StatusOfTask;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Task task1 = new Task("Задача1", "тема задачи 1", StatusOfTask.NEW, 1);
        Task task2 = new Task("Задача2", "тема задачи 2", StatusOfTask.NEW, 1);
        assertEquals(task1, task2, "Сошлось успешно!");
    }
}
