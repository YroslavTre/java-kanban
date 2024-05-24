package tests;

import main.history.InMemoryHistoryManager;
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
    void testAddAndRetrieveHistory() {
        Task task = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void testHistoryLimit() {
        for (int i = 1; i <= 12; i++) {
            historyManager.add(new Task("Задача " + i, "Описание задачи " + i, i, Status.NEW));
        }
        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size());
        assertEquals(3, history.get(0).getId());
    }
}
