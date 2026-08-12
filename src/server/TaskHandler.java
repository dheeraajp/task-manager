package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.CreateTaskRequest;
import model.Task;
import service.TaskService;
import util.LocalDateAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import model.ErrorResponse;

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

        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {

            handleGet(exchange);

        } else if (method.equals("POST")) {

            handlePost(exchange);

        } else if (method.equals("PUT")) {

            handlePut(exchange);

        } else if (method.equals("DELETE")) {

            handleDelete(exchange);

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

        List<Task> tasks = taskService.getAllTasks();

        String json = gson.toJson(tasks);

        sendResponse(
                exchange,
                200,
                json
        );
    }

        private void handlePost(HttpExchange exchange)
                throws IOException {

        try {

                String requestBody = new String(
                        exchange.getRequestBody().readAllBytes()
                );

                CreateTaskRequest request =
                        gson.fromJson(
                                requestBody,
                                CreateTaskRequest.class
                        );

                if (request == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Invalid request\"}"
                );

                return;
                }

                Task task = taskService.addTask(
                        request.getName(),
                        request.getDueDate(),
                        request.getPriority()
                );

                String json = gson.toJson(task);

                sendResponse(
                        exchange,
                        201,
                        json
                );

        } catch (IllegalArgumentException e) {

                String errorJson = gson.toJson(
                        new ErrorResponse(
                                e.getMessage()
                        )
                );

                sendResponse(
                        exchange,
                        400,
                        errorJson
                );

        } catch (RuntimeException e) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Invalid JSON request\"}"
                );
        }
        }

    private void handlePut(HttpExchange exchange)
            throws IOException {

        String path = exchange.getRequestURI().getPath();

        String[] parts = path.split("/");

        if (parts.length != 4) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid task ID\"}"
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(parts[3]);

        } catch (NumberFormatException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid task ID\"}"
            );

            return;
        }

        boolean completed =
                taskService.completeTask(id);

        if (!completed) {

            sendResponse(
                    exchange,
                    404,
                    "{\"error\":\"Task not found\"}"
            );

            return;
        }

        sendResponse(
                exchange,
                200,
                "{\"message\":\"Task completed\"}"
        );
    }

    private void handleDelete(HttpExchange exchange)
            throws IOException {

        String path = exchange.getRequestURI().getPath();

        String[] parts = path.split("/");

        if (parts.length != 4) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid task ID\"}"
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(parts[3]);

        } catch (NumberFormatException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"Invalid task ID\"}"
            );

            return;
        }

        boolean deleted =
                taskService.deleteTask(id);

        if (!deleted) {

            sendResponse(
                    exchange,
                    404,
                    "{\"error\":\"Task not found\"}"
            );

            return;
        }

        sendResponse(
                exchange,
                200,
                "{\"message\":\"Task deleted\"}"
        );
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