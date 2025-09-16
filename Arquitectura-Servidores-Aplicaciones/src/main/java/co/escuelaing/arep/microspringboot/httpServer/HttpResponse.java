
package co.escuelaing.arep.microspringboot.httpServer;

/**
 *
 * @author Juan David Rodríguez
 */

public class HttpResponse {
    private String type = "text/html";

    public void type(String mimeType) { this.type = mimeType; }
    public String getType() { return type; }
}


