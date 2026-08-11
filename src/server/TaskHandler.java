package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.Task;
import service.TaskService;
import util.LocalDateAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private final TaskService taskService;
    private final Gson gson;

    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        new LocalDateAdapter()
                )
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        String method =
                exchange.getRequestMethod();

        if (method.equals("GET")) {
            handleGet(exchange);
        } else {
            sendResponse(
                    exchange,
                    405,
                    "{\"error\":\"Method not allowed\"}"
            );
        }
    }

    private void handleGet(HttpExchange exchange)
            throws IOException {

        List<Task> tasks =
                taskService.getAllTasks();

        String json = gson.toJson(tasks);

        sendResponse(exchange, 200, json);
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        byte[] responseBytes =
                response.getBytes();

        exchange.sendResponseHeaders(
                statusCode,
                responseBytes.length
        );

        OutputStream output =
                exchange.getResponseBody();

        output.write(responseBytes);

        output.close();
    }
}