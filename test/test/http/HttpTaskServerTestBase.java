package test.http;

import com.google.gson.Gson;
import main.http.HttpTaskServer;
import main.manager.InMemoryTaskManager;
import main.manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.net.http.HttpClient;

public class HttpTaskServerTestBase {
    protected TaskManager manager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient httpClient;

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = taskServer.getGson();
        httpClient = HttpClient.newHttpClient();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }
}