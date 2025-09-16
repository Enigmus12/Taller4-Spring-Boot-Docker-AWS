package co.escuelaing.arep.microspringboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import co.escuelaing.arep.microspringboot.httpServer.HttpResponse;

public class HttpResponseTest {

    @Test
    public void testDefaultType() {
        HttpResponse res = new HttpResponse();
        assertEquals("text/html", res.getType());
    }

    @Test
    public void testChangeType() {
        HttpResponse res = new HttpResponse();
        res.type("application/json");
        assertEquals("application/json", res.getType());
    }
}
