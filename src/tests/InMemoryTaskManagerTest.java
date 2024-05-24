package tests;


import main.manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddNewTask() {
        Task task = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask);
        assertEquals(task, savedTask);
    }

    @Test
    void testGetHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 2, Status.NEW);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, Status.NEW, epic.getId());
        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(epic, history.get(1));
        assertEquals(subtask, history.get(2));
    }
}
