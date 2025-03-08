package test;

import main.history.InMemoryHistoryManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.Status;
import main.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу");
        assertEquals(task, history.get(0), "Задача должна совпадать");
    }

    @Test
    void shouldNotContainDuplicates() {
        Task task = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна содержать дубликаты");
    }

    @Test
    void shouldLimitHistorySize() {
        for (int i = 1; i <= 15; i++) {
            historyManager.add(new Task("Task " + i, "Description", i, Status.NEW));
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать не более 10 задач");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description 1", 1, Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", 2, Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после удаления");
        assertFalse(history.contains(task1), "История не должна содержать удаленную задачу");
    }

    @Test
    void shouldClearHistoryWhenAllTasksRemoved() {
        Task task = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления всех задач");
    }

    @Test
    void shouldMoveTaskToEndIfReadded() {
        Task task1 = new Task("Task 1", "Description 1", 1, Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", 2, Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две уникальные задачи");
        assertEquals(task1, history.get(1), "Перезапрошенная задача должна быть в конце");
    }

    @Test
    void shouldRemoveSubtaskFromHistoryWhenDeletedFromEpic() {
        Epic epic = new Epic("Epic 1", "Epic Description", 1, Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", 2, Status.NEW, epic.getId());
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(subtask.getId());
        List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(subtask), "История не должна содержать удаленную подзадачу");
    }

    @Test
    void shouldNotHaveOrphanedSubtaskIdsInEpic() {
        Epic epic = new Epic("Epic 1", "Epic Description", 1, Status.NEW);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", 2, Status.NEW, epic.getId());
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(subtask.getId());
        assertFalse(historyManager.getHistory().contains(subtask), "Эпик не должен хранить удаленные подзадачи");
    }

    @Test
    void shouldRetainCorrectTaskDataAfterModification() {
        Task task = new Task("Task 1", "Description 1", 1, Status.NEW);
        historyManager.add(task);
        task.setStatus(Status.DONE);
        Task retrievedTask = historyManager.getHistory().get(0);
        assertEquals(Status.DONE, retrievedTask.getStatus(), "Измененный статус должен сохраняться в истории");
    }
}