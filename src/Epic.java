import java.util.HashMap;

class Epic extends Task {
    private HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
        this.subtasks = new HashMap<>();
    }

    // Метод для добавления подзадачи к эпику
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    // Метод для завершения эпика
    public void completeEpic() {
        boolean allSubtasksDone = true;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() != Status.DONE) {
                allSubtasksDone = false;
                break;
            }
        }
        if (allSubtasksDone) {
            setStatus(Status.DONE);
        }
    }

    // Метод для вывода списка подзадач на консоль
    public void printSubtasks() {
        System.out.println("Subtasks of Epic '" + getTitle() + "':");
        for (Subtask subtask : subtasks.values()) {
            System.out.println("Title: " + subtask.getTitle());
            System.out.println("Description: " + subtask.getDescription());
            System.out.println("Status: " + subtask.getStatus());
            System.out.println("----------");
        }
    }

    // Геттер для доступа к подзадачам
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
}