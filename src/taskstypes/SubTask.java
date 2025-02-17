package taskstypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;


    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, StatusOfTask status, int id, int epicId) {
        super(taskName, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, StatusOfTask status, int id, Duration duration, LocalDateTime startTime,
                   int epicId) {
        super(taskName, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, StatusOfTask status, int id, int epicId, Duration duration, LocalDateTime startTime) {
        super(taskName, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(taskName, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}
