import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр менеджера задач
        TaskManager taskManager = new TaskManager();

        // Создаем обычную задачу
        Task task = new Task("Переезд", "Описание задачи", 1, Status.NEW);

        // Создаем эпик
        Epic epic = new Epic("Большой проект", "Описание эпика", 2, Status.NEW);

        // Создаем подзадачи и добавляем их к эпику
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 3, Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 4, Status.NEW, epic.getId());
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Меняем статус подзадачи
        subtask1.setStatus(Status.IN_PROGRESS);

        // Завершаем подзадачу
        subtask1.setStatus(Status.DONE);

        // Завершаем эпик
        epic.completeEpic();


        taskManager.createTask(task);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        taskManager.createTask(epic);

        // Выводим список всех задач на консоль
        ArrayList<Task> allTasks = taskManager.getAllTasks();
        for (Task t : allTasks) {
            t.printTask();
            System.out.println("----------");
        }
    }
}