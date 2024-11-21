import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;

    public Epic(String taskName, String description, int id) {
        super(taskName, description, id);
        this.subTasksIds = new ArrayList<>();
    }


    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTasksIds.add(subTaskId);
    }

}
