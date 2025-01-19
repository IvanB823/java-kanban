package ManagersTypes;

import TasksTypes.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addToHistory(Task task){
        remove(task.getId());
        linkLast(task);
        history.put(task.getId(),tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int taskId){
        if (history.containsKey(taskId)) {
            history.remove(taskId);
            removeNode(history.get(taskId));
        }

    }

    private void linkLast(Task task) {
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        Node node = new Node(task);

        if (tail == null) {
            head = node;
            tail = node;
        } else {
            node.setPrev(tail);
            tail.setNext(node);
            tail = node;
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.getTask());
            node = node.getNext();
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.getPrev() == null) {
            head = node.getNext();
            if (head != null) {
                head.setPrev(null);
            }
        } else {
            node.getPrev().setNext(node.getNext());
        }
        if (node.getNext() == null) {
            tail = node.getPrev();
            if (tail != null) {
                tail.setNext(null);
            }
        } else {
            node.getNext().setPrev(node.getPrev());
        }
    }


}

