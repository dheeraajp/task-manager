package service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Priority;
import model.Task;
import repository.TaskRepository;

public class TaskService {

    private final TaskRepository repository;
    private final List<Task> tasks;

    public TaskService(
            TaskRepository repository
    ) throws IOException {

        this.repository = repository;
        this.tasks = repository.loadTasks();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task addTask(
            String name,
            LocalDate dueDate,
            Priority priority
    ) throws IOException {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Task name is required"
            );
        }

        if (dueDate == null) {
            throw new IllegalArgumentException(
                    "Due date is required"
            );
        }

        if (priority == null) {
            throw new IllegalArgumentException(
                    "Priority is required"
            );
        }

        int id = generateNextId();

        Task task = new Task(
                id,
                name.trim(),
                dueDate,
                priority
        );

        tasks.add(task);

        repository.saveTasks(tasks);

        return task;
    }

    public boolean completeTask(
            int id
    ) throws IOException {

        for (Task task : tasks) {

            if (task.getId() == id) {

                task.markCompleted();

                repository.saveTasks(tasks);

                return true;
            }
        }

        return false;
    }

    public boolean deleteTask(
            int id
    ) throws IOException {

        boolean removed = tasks.removeIf(
                task -> task.getId() == id
        );

        if (removed) {
            repository.saveTasks(tasks);
        }

        return removed;
    }

    public List<Task> getActiveTasks() {

        List<Task> activeTasks =
                new ArrayList<>();

        for (Task task : tasks) {

            if (!task.isCompleted()) {
                activeTasks.add(task);
            }
        }

        return activeTasks;
    }

    public List<Task> getCompletedTasks() {

        List<Task> completedTasks =
                new ArrayList<>();

        for (Task task : tasks) {

            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }

        return completedTasks;
    }

    public List<Task> getOverdueTasks() {

        List<Task> overdueTasks =
                new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {

            if (!task.isCompleted()
                    && task.getDueDate().isBefore(today)) {

                overdueTasks.add(task);
            }
        }

        return overdueTasks;
    }

    private int generateNextId() {

        int maxId = 0;

        for (Task task : tasks) {

            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }

        return maxId + 1;
    }
}