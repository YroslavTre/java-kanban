import org.junit.jupiter.api.Test;
import main.tasks.Status;
import main.tasks.Task;

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
}
