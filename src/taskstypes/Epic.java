package taskstypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description);
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(String taskName, String description, int id, ArrayList<Integer> subTasksIds) {
        super(taskName, description);
        this.setId(id);
        this.subTasksIds = subTasksIds;
    }

    public Epic(String taskName, String description, int id) { // исключительно для тестов
        super(taskName, description);
        this.setId(id);
    }

    public Epic(String taskName, String description, Duration duration, LocalDateTime startTime, int id) {
        super(taskName, description, duration, startTime);
        this.setId(id);
    }




    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subTasksIds.add(subtaskId);

        }
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

}
