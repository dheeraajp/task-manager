import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        server.createContext("/", exchange -> {

            String response = "Hello from my Java Task Manager!";

            exchange.sendResponseHeaders(
                    200,
                    response.getBytes().length
            );

            exchange.getResponseBody().write(
                    response.getBytes()
            );

            exchange.getResponseBody().close();
        });

        server.start();

        System.out.println(
                "Server running at http://localhost:8080"
        );
    }
}