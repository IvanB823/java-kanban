package managerstypes;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

    HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHttpHandler());
        httpServer.createContext("/subtasks", new SubtasksHttpHandler());
        httpServer.createContext("/epics", new EpicsHttpHandler());
        httpServer.createContext("/history", new HistoryHttpHandler());
        httpServer.createContext("/prioritized", new PrioritizedHttpHandler());
    httpServer.start();

    }
}
