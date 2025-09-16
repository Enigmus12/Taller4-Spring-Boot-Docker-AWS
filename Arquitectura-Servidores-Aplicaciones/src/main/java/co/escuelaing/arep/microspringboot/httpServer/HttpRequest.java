package co.escuelaing.arep.microspringboot.httpServer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Juan David Rodríguez
 */

public class HttpRequest {
    private String path;
    private String query;
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(String path) {
        this.path = path;
        if (path.contains("?")) {
            this.query = path.split("\\?", 2)[1];
            parseQuery();
        }
    }

    private void parseQuery() {
        for (String p : query.split("&")) {
            String[] kv = p.split("=");
            if (kv.length == 2) {
                queryParams.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            } else if (kv.length == 1) {
                queryParams.put(kv[0], "");
            }
        }
    }

    public String getPath() { return path.contains("?") ? path.split("\\?")[0] : path; }
    public String getQueryParam(String key) { return queryParams.get(key); }
}

