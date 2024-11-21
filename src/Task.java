import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    int id;
    private StatusOfTask status;

    public Task(String taskName, String description, int id){
        this.taskName = taskName;
        this.description = description;
        this.id = id;
        this.status = StatusOfTask.NEW;
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

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false; // проверяем ссылку на null
        if (this.getClass() != obj.getClass()) return false; // сравниваем классы
        Task task = (Task) obj; // открываем доступ к полям другого объекта
        return Objects.equals(taskName, task.taskName) && // проверяем все поля
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status) && // нужно логическое «и»
                (id == task.id); // примитивы сравниваем через ==
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, description, id, status);
    }
}


