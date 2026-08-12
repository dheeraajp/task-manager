package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticFileHandler implements HttpHandler {

    private final Path webDirectory;

    public StaticFileHandler(String webDirectory) {
        this.webDirectory = Path.of(webDirectory);
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        String requestPath =
                exchange.getRequestURI().getPath();

        if (requestPath.equals("/")) {
            requestPath = "/index.html";
        }

        Path filePath = webDirectory.resolve(
                requestPath.substring(1)
        );

        if (!Files.exists(filePath)) {

            String response = "404 - File Not Found";

            exchange.sendResponseHeaders(
                    404,
                    response.getBytes().length
            );

            OutputStream output =
                    exchange.getResponseBody();

            output.write(response.getBytes());
            output.close();

            return;
        }

        String contentType =
                getContentType(filePath);

        exchange.getResponseHeaders().set(
                "Content-Type",
                contentType
        );

        byte[] fileBytes =
                Files.readAllBytes(filePath);

        exchange.sendResponseHeaders(
                200,
                fileBytes.length
        );

        OutputStream output =
                exchange.getResponseBody();

        output.write(fileBytes);
        output.close();
    }

    private String getContentType(Path filePath) {

        String fileName =
                filePath.toString();

        if (fileName.endsWith(".html")) {
            return "text/html";
        }

        if (fileName.endsWith(".css")) {
            return "text/css";
        }

        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }

        return "text/plain";
    }
}