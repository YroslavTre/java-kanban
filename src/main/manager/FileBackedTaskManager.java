package main.manager;

import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
        private final File file;

        public FileBackedTaskManager(File file) {
            this.file = file;
        }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    private void save() throws ManagerSaveException {
        String setOfParameters = "id,type,name,status,description,epic";

        try (BufferedWriter writeTask = new BufferedWriter(new FileWriter(file))) {
            writeTask.write(setOfParameters);
            writeTask.newLine();

            for (Task task : getTasks()) {
                String taskString = taskToString(task);
                writeTask.write(taskString);
                writeTask.newLine();
            }

            for (Epic epic : getEpics()) {
                String epicString = taskToString(epic);
                writeTask.write(epicString);
                writeTask.newLine();
            }

            for (Subtask subtask : getSubtasks()) {
                String subtaskString = taskToString(subtask);
                writeTask.write(subtaskString);
                writeTask.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл", e);
        }
    }

    private String taskToString(Task task) {
        StringBuilder setTask = new StringBuilder();

        setTask.append(task.getId()).append(",");

        if (task instanceof Epic) {
            setTask.append("EPIC,");
        } else if (task instanceof Subtask) {
            setTask.append("SUBTASK,");
        } else {
            setTask.append("TASK,");
        }

        setTask.append(task.getTitle()).append(",");
        setTask.append(task.getStatus()).append(",");
        setTask.append(task.getDescription());

        if (task instanceof Subtask) {
            setTask.append(",").append(((Subtask) task).getEpicId());
        }

        return setTask.toString();
    }

    private Task taskFromString(String line) {
        String[] element = line.split(",");
        int id = Integer.parseInt(element[0]);
        String type = element[1];
        String title = element[2];
        Status status = Status.valueOf(element[3]);
        String description = element[4];

        switch (type) {
            case "TASK":
                return new Task(title, description, id, status);
            case "EPIC":
                return new Epic(title, description, id, status);
            case "SUBTASK":
                int epicId = Integer.parseInt(element[5]);
                return new Subtask(title, description, id, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            String data = Files.readString(file.toPath());
            String[] lines = data.split("\n");

            for (int i = 1; i < lines.length; i++) {
                Task task = manager.taskFromString(lines[i]);
                if (task instanceof Epic) {
                    System.out.println("Загрузка эпика: " + task);
                    manager.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else {
                    manager.createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных из файла", e);
        }

        return manager;
    }
}
