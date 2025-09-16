package co.escuelaing.arep.microspringboot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import co.escuelaing.arep.microspringboot.httpServer.HttpServer;

public class HttpServerTest {

    @BeforeAll
    public static void startServer() {
        new Thread(() -> {
            try {
                HttpServer.runServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(2000); // Esperar que el servidor arranque
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getResponse(String endpoint) throws Exception {
        URL url = new URL("http://localhost:8080" + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    @Test
    public void testGreetingEndpoint() throws Exception {
        String response = getResponse("/greeting?name=Juan");
        assertTrue(response.contains("Hola Juan"));
    }

    @Test
    public void testSumEndpoint() throws Exception {
        String response = getResponse("/sum?x=2&y=3");
        assertTrue(response.contains("Resultado: 5"));
    }

    @Test
    public void testPiEndpoint() throws Exception {
        String response = getResponse("/pi");
        assertEquals(String.valueOf(Math.PI), response);
    }
}
