package test;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testAddSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW, epic.getId());
        epic.addSubtask(subtask);
        assertTrue(epic.getSubtasks().containsKey(subtask.getId()));
    }

    @Test
    void testCompleteEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, Status.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 3, Status.DONE, epic.getId());
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, epic.getStatus());
    }
}
