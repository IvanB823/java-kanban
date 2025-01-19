package TasksTypes;

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



    public int getEpicId() {
        return epicId;
    }

}
