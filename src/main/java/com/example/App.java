package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(
            System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(
            new InetSocketAddress("0.0.0.0", port), 0
        );

        server.createContext("/", exchange -> {
            String html = """
                <!DOCTYPE html>
                <html>
                <head><title>Jenkins Docker App</title></head>
                <body>
                    <h1>Hello from Jenkins, Docker and Rajendra.!</h1>
                    <p>Your Java web application is running super fast.</p>
                </body>
                </html>
                """;

            byte[] response = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set(
                "Content-Type", "text/html; charset=UTF-8"
            );
            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream out = exchange.getResponseBody()) {
                out.write(response);
            }
        });

        server.start();
        System.out.println("Web application listening on port " + port);
    }
}
