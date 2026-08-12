import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import repository.TaskRepository;
import server.StaticFileHandler;
import server.TaskHandler;
import service.TaskService;

public class Main {

    public static void main(String[] args)
            throws IOException {

        // Handles saving/loading tasks from tasks.json
        TaskRepository repository =
                new TaskRepository(
                        "data/tasks.json"
                );

        // Handles task business logic
        TaskService service =
                new TaskService(repository);

        // Create a server that listens on port 8080
        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        // REST API
        server.createContext(
                "/api/tasks",
                new TaskHandler(service)
        );

        // Frontend files
        server.createContext(
                "/",
                new StaticFileHandler("web")
        );

        // Start the server
        server.start();

        System.out.println(
                "Server running at http://localhost:8080"
        );
    }
}