package main.history;

import main.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LIMIT = 10;
    private final LinkedList<Task> history = new LinkedList<>();
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        Node node = new Node(task);

        if (task == null) {
            return;
        }

        remove(task.getId());

        if (nodeMap.size() == HISTORY_LIMIT) {
            nodeMap.remove(head.task.getId());
            removeNode(head);
        }


        linkLast(node);
        nodeMap.put(task.getId(), node);
    }

    private void linkLast(Node node) {
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node == head) {
            head = node.next;
        }
        if (node == tail) {
            tail = node.prev;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.next = null;
        node.prev = null;
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task) {
            this.task = task;
        }
    }
}