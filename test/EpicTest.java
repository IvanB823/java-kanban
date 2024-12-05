import org.junit.jupiter.api.Test;
import tasksTypes.Epic;
import tasksTypes.StatusOfTask;
import tasksTypes.Task;
import managersTypes.InMemoryHistoryManager;
import managersTypes.InMemoryTaskManager;
import managersTypes.Managers;
import managersTypes.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик2", "тема эпика 2");
        System.out.println(epic1.getId());
        Epic epic2 = new Epic("Эпик2", "тема эпика 2");
        assertEquals(epic1, epic2, "Сошлось успешно!");
        System.out.println(epic2.getId());
    }

}
