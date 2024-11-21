public class SubTask extends Task {
    private final int epicId;


    public SubTask(String taskName, String description, int id, int epicId) {
        super(taskName, description, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}
