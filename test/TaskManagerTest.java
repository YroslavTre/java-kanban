import main.manager.TaskManager;
import main.tasks.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void testCreateAndGetTask() {
        Task task = new Task("Task", "Description", 1, Status.NEW);
        taskManager.createTask(task);
        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask);
        assertEquals(task, savedTask);
    }

    @Test
    void testCreateAndGetEpic() {
        Epic epic = new Epic("Epic", "Description", 1, Status.NEW);
        taskManager.createEpic(epic);
        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(savedEpic);
        assertEquals(epic, savedEpic);
    }

    @Test
    void testCreateAndGetSubtask() {
        Epic epic = new Epic("Epic", "Description", 1, Status.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", 2, Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(savedSubtask);
        assertEquals(subtask, savedSubtask);
        assertEquals(epic.getId(), savedSubtask.getEpicId());
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task", "Description", 1, Status.NEW);
        taskManager.createTask(task);
        task.setStatus(Status.DONE);
        taskManager.updateTask(task);
        assertEquals(Status.DONE, taskManager.getTask(task.getId()).getStatus());
    }

    @Test
    void testUpdateSubtask() {
        Epic epic = new Epic("Epic", "Description", 1, Status.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", 2, Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        assertEquals(Status.DONE, taskManager.getSubtask(subtask.getId()).getStatus());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("Task", "Description", 1, Status.NEW);
        taskManager.createTask(task);
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()));
    }

    @Test
    void testDeleteEpicWithSubtasks() {
        Epic epic = new Epic("Epic", "Description", 1, Status.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", 2, Status.NEW, epic.getId());
        taskManager.createSubtask(subtask);
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()));
        assertNull(taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task1", "Description", 1, Status.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));
        Task task2 = new Task("Task2", "Description", 2, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.createTask(task2);
        taskManager.createTask(task1);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(task2, prioritized.get(0));
        assertEquals(task1, prioritized.get(1));
    }

    @Test
    void testTaskTimeOverlap() {
        Task task1 = new Task("Task1", "Description", 1, Status.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
        taskManager.createTask(task1);

        Task task2 = new Task("Task2", "Description", 2, Status.NEW,
                LocalDateTime.now().plusMinutes(30), Duration.ofHours(1));

        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }

    @Test
    void testEpicStatusWithMixedSubtasks() {
        Epic epic = new Epic("Epic", "Description", 1, Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub1", "Desc", 2, Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Sub2", "Desc", 3, Status.DONE, epic.getId());
        Subtask subtask3 = new Subtask("Sub3", "Desc", 4, Status.IN_PROGRESS, epic.getId());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus());
    }
}