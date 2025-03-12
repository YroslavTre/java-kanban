import main.manager.FileBackedTaskManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(""); // Очищаем файл
        }
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(loadedManager.getEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(loadedManager.getSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void testSaveMultipleTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 2, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, Status.NEW, epic1.getId());

        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = loadedManager.getTasks();
        List<Epic> epics = loadedManager.getEpics();
        List<Subtask> subtasks = loadedManager.getSubtasks();

        assertEquals(1, tasks.size(), "Должна быть одна задача");
        assertEquals(task1, tasks.get(0), "Задачи должны совпадать");

        assertEquals(1, epics.size(), "Должен быть один эпик");
        assertEquals(epic1, epics.get(0), "Эпики должны совпадать");

        assertEquals(1, subtasks.size(), "Должна быть одна подзадача");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи должны совпадать");
    }

    @Test
    void testLoadMultipleTasks() throws IOException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 2, Status.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, Status.NEW, epic1.getId());

        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);

        String fileContent = Files.readString(tempFile.toPath());
        assertTrue(fileContent.contains("Задача 1"), "Файл должен содержать задачу 1");
        assertTrue(fileContent.contains("Эпик 1"), "Файл должен содержать эпик 1");
        assertTrue(fileContent.contains("Подзадача 1"), "Файл должен содержать подзадачу 1");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = loadedManager.getTasks();
        List<Epic> epics = loadedManager.getEpics();
        List<Subtask> subtasks = loadedManager.getSubtasks();

        assertEquals(1, tasks.size(), "Должна быть одна задача");
        assertEquals(task1, tasks.get(0), "Задачи должны совпадать");

        assertEquals(1, epics.size(), "Должен быть один эпик");
        assertEquals(epic1, epics.get(0), "Эпики должны совпадать");

        assertEquals(1, subtasks.size(), "Должна быть одна подзадача");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи должны совпадать");
    }

    @Test
    void testDeleteTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        manager.createTask(task1);

        manager.deleteTask(task1.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getTasks().isEmpty(), "Задача должна быть удалена");
    }

    @Test
    void testUpdateTask() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, Status.NEW);
        manager.createTask(task1);

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask = loadedManager.getTask(task1.getId());
        assertEquals(Status.IN_PROGRESS, loadedTask.getStatus(), "Статус задачи должен быть обновлён");
    }
}