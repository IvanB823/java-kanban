package tasksTypes;

import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    private int id;
    private StatusOfTask status;

    public Task(String taskName, String description){
        this.taskName = taskName;
        this.description = description;
        this.status = StatusOfTask.NEW;
    }

    public Task(String taskName, String description, StatusOfTask status, int id){
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}


