import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import repository.TaskRepository;
import server.TaskHandler;
import service.TaskService;

public class Main {

    public static void main(String[] args)
            throws IOException {

        TaskRepository repository =
                new TaskRepository(
                        "data/tasks.json"
                );

        TaskService service =
                new TaskService(repository);

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        server.createContext(
                "/api/tasks",
                new TaskHandler(service)
        );

        server.start();

        System.out.println(
                "Server running at http://localhost:8080"
        );
    }
}