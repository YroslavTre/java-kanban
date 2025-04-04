package main;

import main.manager.Managers;
import main.manager.TaskManager;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создаем задачи
        Task task1 = new Task("Задача 1", "Описание задачи 1", 0, Status.NEW);
        manager.createTask(task1);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 0, Status.NEW);
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 0,
                Status.NEW, epic1.getId(), LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 0, Status.NEW,
                epic1.getId(), LocalDateTime.of(2025, 1, 1, 10, 0),
                Duration.ofHours(1));
        manager.createSubtask(subtask2);

        // Вызов методов для добавления в историю
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());

        // Печать всех задач и истории
        printAllTasks(manager);
    }
}
