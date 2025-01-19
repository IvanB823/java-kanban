package TestOfTasksTypes;

import org.junit.jupiter.api.Test;
import TasksTypes.Epic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EpicTest {


    @Test
    void shouldReturnTrueIfIdsAreEquals() {
        Epic epic1 = new Epic("Эпик2", "тема эпика 2", 2);
        System.out.println(epic1.getId());
        Epic epic2 = new Epic("Эпик3", "тема эпика 3", 2);
        assertEquals(epic1, epic2, "Ошибка сверки!");
        System.out.println(epic2.getId());
    }
    @Test
    void TestOfPoint3() {
        Epic epic1 = new Epic("Эпик2", "тема эпика 2", 2);
        epic1.addSubtask(epic1.getId());
        assertNull(epic1.getSubTasksIds());
    }

}
