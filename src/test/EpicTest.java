package test;

import org.junit.jupiter.api.Test;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;

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

    @Test
    void testUpdateStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 2, Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 3, Status.NEW, epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW, если все подзадачи имеют статус NEW");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        epic.setStatus(Status.DONE);

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть DONE, если все подзадачи имеют статус DONE");

        subtask2.setStatus(Status.IN_PROGRESS);
        epic.setStatus(Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS, если хотя бы одна подзадача не выполнена");
    }
}
