package test;


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
    void testAddAndGetEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        taskManager.createEpic(epic);
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(retrievedEpic, "Эпик должен быть найден");
        assertEquals(epic, retrievedEpic, "Эпики должны совпадать");
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(retrievedSubtask, "Подзадача должна быть найдена");
        assertEquals(subtask, retrievedSubtask, "Подзадачи должны совпадать");
    }

    @Test
    void testGetHistory() {
        Task task = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        taskManager.createTask(task);
        taskManager.getTask(task.getId());

        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 2, Status.NEW);
        taskManager.createEpic(epic);
        taskManager.getEpic(epic.getId());

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        taskManager.getSubtask(subtask.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        assertEquals(task, history.get(0), "Первой в истории должна быть 'Задача 1'");
        assertEquals(epic, history.get(1), "Второй в истории должен быть 'Эпик 1'");
        assertEquals(subtask, history.get(2), "Третьей в истории должна быть 'Подзадача 1'");
    }

    @Test
    void testTaskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Task task2 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }
}
