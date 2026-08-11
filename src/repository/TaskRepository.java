package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Task;
import util.LocalDateAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private final Path filePath;
    private final Gson gson;

    public TaskRepository(String fileName) {

        this.filePath = Path.of(fileName);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        new LocalDateAdapter()
                )
                .setPrettyPrinting()
                .create();
    }

    public void saveTasks(List<Task> tasks) throws IOException {

        String json = gson.toJson(tasks);

        Files.writeString(filePath, json);
    }

    public List<Task> loadTasks() throws IOException {

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        String json = Files.readString(filePath);

        if (json.isBlank()) {
            return new ArrayList<>();
        }

        List<Task> tasks = gson.fromJson(
                json,
                new TypeToken<List<Task>>() {}.getType()
        );

        if (tasks == null) {
            return new ArrayList<>();
        }

        return tasks;
    }
}