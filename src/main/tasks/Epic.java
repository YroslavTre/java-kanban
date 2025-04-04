package main.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
        this.subtasks = new HashMap<>();
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return subtasks.values().stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public Duration getDuration() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return subtasks.values().stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return subtasks.values().stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
