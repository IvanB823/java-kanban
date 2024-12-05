import org.junit.jupiter.api.Test;
import tasksTypes.Epic;
import tasksTypes.StatusOfTask;
import tasksTypes.SubTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import managersTypes.InMemoryTaskManager;

public class SubTaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик1", "Эпик1");
        SubTask subtask1 = new SubTask("NEW Задача2", "NEW тема ПОДзадачи 2", StatusOfTask.NEW, 4, inMemoryTaskManager.getSubTask(4).getEpicId());
        SubTask subtask2 = new SubTask("NEW Задача3", "NEW тема ПОДзадачи 3", StatusOfTask.NEW, 4, inMemoryTaskManager.getSubTask(4).getEpicId());
        assertEquals(subtask1, subtask2);
    }
}
