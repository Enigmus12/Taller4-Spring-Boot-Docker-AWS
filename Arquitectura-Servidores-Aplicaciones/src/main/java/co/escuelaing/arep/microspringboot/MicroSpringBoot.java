package co.escuelaing.arep.microspringboot;

import co.escuelaing.arep.microspringboot.httpServer.HttpServer;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Juan David Rodríguez
 */
public class MicroSpringBoot {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Starting MicroSpringBoot:");
        HttpServer.runServer();
    }

}
