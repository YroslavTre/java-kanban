import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {
    private HashMap<Integer, Task> tasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    // Получение списка всех задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Удаление всех задач
    public void removeAllTasks() {
        tasks.clear();
    }

    // Получение задачи по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Создание задачи
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // Обновление задачи
    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    // Удаление задачи по идентификатору
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    // Получение списка всех подзадач определенного эпика
    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Subtask && ((Subtask) task).getEpicId() == epicId) {
                subtasks.add((Subtask) task);
            }
        }
        return subtasks;
    }

    // Метод для управления статусами эпиков
    public void manageEpicStatus() {
        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                Epic epic = (Epic) task;
                boolean allSubtasksDone = epic.getSubtasks().values().stream()
                        .allMatch(subtask -> subtask.getStatus() == Status.DONE);

                if (epic.getSubtasks().isEmpty() || allSubtasksDone) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }
}