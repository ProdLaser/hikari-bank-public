import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Public, read-only demonstration: does not load or expose Bank.java or account data. */
public final class PublicPreviewServer {
    private static final Path ROOT = Path.of("public").toAbsolutePath().normalize();

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/", PublicPreviewServer::serve);
        server.start();
        System.out.println("Public preview listening on port " + port);
    }

    private static void serve(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        var headers = exchange.getResponseHeaders();
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("Referrer-Policy", "no-referrer");
        headers.set("X-Frame-Options", "DENY");
        headers.set("Content-Security-Policy", "default-src 'none'; style-src 'self'; img-src 'self'; base-uri 'none'; form-action 'none'; frame-ancestors 'none'");
        headers.set("Cache-Control", "no-store");
        if (!method.equals("GET") && !method.equals("HEAD")) {
            respond(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed".getBytes(StandardCharsets.UTF_8));
            return;
        }
        String path = exchange.getRequestURI().getPath();
        String filename = path.equals("/") || path.equals("/index.html") ? "index.html"
                : path.equals("/styles.css") ? "styles.css" : null;
        if (filename == null) {
            respond(exchange, 404, "text/plain; charset=utf-8", "Not Found".getBytes(StandardCharsets.UTF_8));
            return;
        }
        byte[] data = Files.readAllBytes(ROOT.resolve(filename));
        respond(exchange, 200, filename.endsWith(".css") ? "text/css; charset=utf-8" : "text/html; charset=utf-8", data);
    }

    private static void respond(HttpExchange exchange, int status, String type, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", type);
        if (exchange.getRequestMethod().equals("HEAD")) {
            exchange.sendResponseHeaders(status, -1);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) { out.write(bytes); }
    }
}
