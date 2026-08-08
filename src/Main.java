import java.time.LocalDate;
import model.Priority;
import model.Task;

public class Main {

    public static void main(String[] args) {

        Task task = new Task(
                1,
                "Finish CS homework",
                LocalDate.of(2026, 8, 15),
                Priority.HIGH
        );

        System.out.println("Name: " + task.getName());
        System.out.println("Due: " + task.getDueDate());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Completed: " + task.isCompleted());

        task.markCompleted();

        System.out.println(
                "Completed after update: " + task.isCompleted()
        );
    }
}