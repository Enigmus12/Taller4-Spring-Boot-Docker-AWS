package co.escuelaing.arep.microspringboot.examples;

import co.escuelaing.arep.microspringboot.annotations.RestController;
import co.escuelaing.arep.microspringboot.annotations.GetMapping;
import co.escuelaing.arep.microspringboot.annotations.RequestParam;

/**
 *
 * @author Juan
 */
@RestController
public class GreetingController {
    
    // Endpoint original (greeting)
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }

    // Endpoint 2 -> /sum
    @GetMapping("/sum")
    public String sum(
        @RequestParam(value = "x", defaultValue = "0") String x,
        @RequestParam(value = "y", defaultValue = "0") String y
    ) {
        try {
            int a = Integer.parseInt(x);
            int b = Integer.parseInt(y);
            return "Resultado: " + (a + b);
        } catch (NumberFormatException e) {
            return "Error: parámetros inválidos";
        }
    }

    // Endpoint 3 -> /pi
    @GetMapping("/pi")
    public String pi() {
        return String.valueOf(Math.PI);
    }
}
