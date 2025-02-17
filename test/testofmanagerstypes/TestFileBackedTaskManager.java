package testofmanagerstypes;

import managerstypes.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import taskstypes.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestFileBackedTaskManager extends TestTaskManager<FileBackedTaskManager> {
    private final File file = File.createTempFile("file", ".csv", null);

    TestFileBackedTaskManager() throws IOException {
    }

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(file);
    }

    @Test
    void shouldSaveAndLoadFromFile() {
        FileBackedTaskManager newManager = new FileBackedTaskManager(file);
        Task task = new Task("Test Task", "Description",
                Duration.ofMinutes(60),
                LocalDateTime.now());
        newManager.addTask(task);

        ArrayList<Task> loadedTasks = newManager.getAllTasks();

        assertFalse(loadedTasks.isEmpty());
        assertEquals(task.getTaskName(), loadedTasks.getFirst().getTaskName());
    }
}