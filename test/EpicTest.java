
import main.manager.Managers;
import main.manager.TaskManager;
import org.junit.jupiter.api.Test;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testAddSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", 2,
                Status.NEW, epic.getId(),
                LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        epic.addSubtask(subtask);
        assertTrue(epic.getSubtasks().containsKey(subtask.getId()));
    }

    @Test
    void testCompleteEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 2,
                Status.DONE, epic.getId(), LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 3,
                Status.DONE, epic.getId(),
                LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void testUpdateStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", 1, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 2,
                Status.NEW, epic.getId(), LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 3,
                Status.NEW, epic.getId(), LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));

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

    @Test
    void testEpicStatusWithMixedSubtasks() {
        // 1. Создаем менеджер задач (который будет обновлять статус эпика)
        TaskManager manager = Managers.getDefault();

        // 2. Создаем и добавляем эпик
        Epic epic = new Epic("Epic", "Desc", 1, Status.NEW);
        manager.createEpic(epic);

        // 3. Создаем подзадачи с разными статусами
        Subtask sub1 = new Subtask("Sub1", "Desc", 2, Status.NEW, 1,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        Subtask sub2 = new Subtask("Sub2", "Desc", 3, Status.DONE, 1,
                LocalDateTime.of(2025, 1, 1, 12, 0),
                Duration.ofHours(1));
        Subtask sub3 = new Subtask("Sub3", "Desc", 4, Status.IN_PROGRESS, 1,
                LocalDateTime.of(2025, 1, 1, 14, 0),
                Duration.ofHours(1));

        // 4. Добавляем подзадачи через менеджер (чтобы статус эпика обновился)
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);
        manager.createSubtask(sub3);

        // 5. Получаем обновленный эпик из менеджера
        Epic updatedEpic = manager.getEpic(epic.getId());

        // 6. Проверяем статус
        assertEquals(Status.IN_PROGRESS, updatedEpic.getStatus(),
                "Статус эпика должен быть IN_PROGRESS при наличии подзадач с разными статусами");
    }

    @Test
    void testEpicTimeWithNoSubtasks() {
        Epic epic = new Epic("Epic", "Desc", 1, Status.NEW);
        assertNull(epic.getStartTime());
        assertNull(epic.getDuration());
    }

    @Test
    void testSubtaskWithEpicLink() {
        Subtask subtask = new Subtask("Subtask", "Desc", 1, Status.NEW, 100,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        assertEquals(100, subtask.getEpicId());
    }

    @Test
    void testSubtaskTimeFields() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);
        Subtask subtask = new Subtask("Subtask", "Desc", 1, Status.NEW,
                100, startTime, duration);

        assertEquals(startTime, subtask.getStartTime());
        assertEquals(duration, subtask.getDuration());
    }
}
