package testOfManagersTypes;

import managersTypes.HistoryManager;
import managersTypes.Managers;
import managersTypes.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestOfManagers {

    @Test
    void getDefaultReturnsInitializedTaskManager() {
        TaskManager manager1 = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();
        assertNotNull(manager1);
        assertNotNull(manager2);
        assertNotEquals(manager1, manager2);
    }

    @Test
    void getDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager manager3 = Managers.getDefaultHistory();
        HistoryManager manager4 = Managers.getDefaultHistory();
        assertNotNull(manager3);
        assertNotNull(manager4);
        assertNotEquals(manager3, manager4);
    }
}
