package co.escuelaing.arep.microspringboot.httpServer;

/**
 *
 * @author Juan David Rodríguez
 */
public interface Service {
    
    public String invoke(HttpRequest req, HttpResponse res);
    
}
