import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

import model.Priority;
import model.Task;
import util.LocalDateAdapter;

public class Main {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        new LocalDateAdapter()
                )
                .setPrettyPrinting()
                .create();

        Task task = new Task(
                1,
                "Finish CS homework",
                LocalDate.of(2026, 8, 15),
                Priority.HIGH
        );

        String json = gson.toJson(task);

        System.out.println(json);

        Task loadedTask = gson.fromJson(json, Task.class);

        System.out.println();
        System.out.println("Loaded task:");
        System.out.println("Name: " + loadedTask.getName());
        System.out.println("Due: " + loadedTask.getDueDate());
        System.out.println("Priority: " + loadedTask.getPriority());
        System.out.println("Completed: " + loadedTask.isCompleted());
    }
}