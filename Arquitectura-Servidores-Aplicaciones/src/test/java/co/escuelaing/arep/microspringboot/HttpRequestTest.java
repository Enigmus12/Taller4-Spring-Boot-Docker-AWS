package co.escuelaing.arep.microspringboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import co.escuelaing.arep.microspringboot.httpServer.HttpRequest;

public class HttpRequestTest {

    @Test
    public void testGetPathWithoutQuery() {
        HttpRequest req = new HttpRequest("/greeting");
        assertEquals("/greeting", req.getPath());
    }

    @Test
    public void testGetPathWithQuery() {
        HttpRequest req = new HttpRequest("/greeting?name=Juan");
        assertEquals("/greeting", req.getPath());
        assertEquals("Juan", req.getQueryParam("name"));
    }

    @Test
    public void testQueryParamWithEmptyValue() {
        HttpRequest req = new HttpRequest("/greeting?name=");
        assertEquals("", req.getQueryParam("name"));
    }

    @Test
    public void testMultipleParams() {
        HttpRequest req = new HttpRequest("/sum?x=10&y=5");
        assertEquals("10", req.getQueryParam("x"));
        assertEquals("5", req.getQueryParam("y"));
    }
}
