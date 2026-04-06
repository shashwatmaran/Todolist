import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodolistService {
    private final List<Task> tasks;
    private int nextId;

    public TodolistService() {
        this.tasks = new ArrayList<>();
        this.nextId = 1;
    }

    public Task addTask(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        Task task = new Task(nextId++, title.trim(), description);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> getTaskById(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public boolean updateTask(int id, String title, String description, boolean completed) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        Optional<Task> taskOpt = getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTitle(title.trim());
            task.setDescription(description);
            task.setCompleted(completed);
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public boolean markTaskCompleted(int id) {
        Optional<Task> taskOpt = getTaskById(id);
        if (taskOpt.isPresent()) {
            taskOpt.get().setCompleted(true);
            return true;
        }
        return false;
    }

    public List<Task> getTasksByStatus(boolean completed) {
        return tasks.stream()
                .filter(t -> t.isCompleted() == completed)
                .toList();
    }

    public static void main(String[] args) {
        TodolistService service = new TodolistService();

        service.addTask("Buy groceries", "Milk, eggs, bread");
        service.addTask("Clean house", "Vacuum and mop floors");
        service.addTask("Study Java", "Practice CRUD operations");

        System.out.println("=== All Tasks ===");
        service.getAllTasks().forEach(System.out::println);

        service.updateTask(1, "Buy groceries (urgent)", "Milk, eggs, bread, butter", false);
        System.out.println("\n=== After Update ===");
        System.out.println(service.getTaskById(1).orElse(null));

        service.markTaskCompleted(2);
        System.out.println("\n=== Completed Tasks ===");
        service.getTasksByStatus(true).forEach(System.out::println);

        service.deleteTask(3);
        System.out.println("\n=== After Deletion (task 3 removed) ===");
        service.getAllTasks().forEach(System.out::println);

        System.out.println("\nTotal tasks: " + service.getTaskCount());
    }
}