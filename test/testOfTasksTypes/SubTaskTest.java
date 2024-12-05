package testOfTasksTypes;

import org.junit.jupiter.api.Test;
import tasksTypes.Epic;
import tasksTypes.StatusOfTask;
import tasksTypes.SubTask;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SubTaskTest {


    @Test
    void shouldReturnTrueIfIdsAreEqual() {
        Epic epic1 = new Epic("Эпик1", "Эпик1");
        SubTask subtask1 = new SubTask("Подзадача2", "тема Подзадачи 2", StatusOfTask.NEW, 4,3);
        SubTask subtask2 = new SubTask("Подзадача3", "тема Подзадачи 3", StatusOfTask.NEW, 4,3);
        assertEquals(subtask1, subtask2,"Ошибка сверки!");
    }
}
