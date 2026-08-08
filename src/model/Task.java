package model;

import java.time.LocalDate;

public class Task {

    private int id;
    private String name;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;

    public Task(int id, String name, LocalDate dueDate, Priority priority) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}