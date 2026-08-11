package model;

import java.time.LocalDate;

public class CreateTaskRequest {

    private String name;
    private LocalDate dueDate;
    private Priority priority;

    public String getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }
}