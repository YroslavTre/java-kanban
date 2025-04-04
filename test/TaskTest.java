import org.junit.jupiter.api.Test;
import main.tasks.Status;
import main.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testEqualsAndHashCode() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Task task2 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testNotEquals() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, Status.NEW);
        assertNotEquals(task1, task2);
    }

    @Test
    void testTaskTimeFields() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);
        Task task = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW, startTime, duration);

        assertEquals(startTime, task.getStartTime());
        assertEquals(startTime.plus(duration), task.getEndTime());
    }

    @Test
    void testTaskWithoutTime() {
        Task task = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        assertNull(task.getStartTime());
        assertNull(task.getEndTime());
    }
}
