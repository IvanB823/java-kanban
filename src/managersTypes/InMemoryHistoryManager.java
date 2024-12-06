package managersTypes;

import tasksTypes.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int historySlotsAmount = 10;

    @Override
    public void addToHistory(Task task){
        history.add(task);
        if (history.size() > historySlotsAmount){
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }

}
