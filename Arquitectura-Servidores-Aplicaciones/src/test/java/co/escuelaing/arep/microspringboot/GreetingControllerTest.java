package co.escuelaing.arep.microspringboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import co.escuelaing.arep.microspringboot.examples.GreetingController;

public class GreetingControllerTest {

    GreetingController controller = new GreetingController();

    @Test
    public void testGreetingWithName() {
        String result = controller.greeting("Juan");
        assertEquals("Hola Juan", result);
    }

    @Test
    public void testGreetingWithDefault() {
        String result = controller.greeting("World");
        assertEquals("Hola World", result);
    }

    @Test
    public void testSumValidNumbers() {
        String result = controller.sum("3", "4");
        assertEquals("Resultado: 7", result);
    }

    @Test
    public void testSumInvalidNumbers() {
        String result = controller.sum("a", "b");
        assertEquals("Error: parámetros inválidos", result);
    }

    @Test
    public void testPi() {
        String result = controller.pi();
        assertEquals(String.valueOf(Math.PI), result);
    }
}
