
package co.escuelaing.arep.microspringboot.httpServer;

import java.net.*;
import java.nio.file.Files;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 * @author Juan David Rodríguez
 */


public class HttpServer {

    private String staticFolder = "www";
    private Map<String, Route> getRoutes = new HashMap<>();

    @FunctionalInterface
    public interface Route {
        String handle(HttpRequest req, HttpResponse res);
    }

    public void staticfiles(String folder) {
        staticFolder = folder;
    }

    public void get(String path, Route route) {
        getRoutes.put(path, route);
    }

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en http://localhost:" + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleRequest(clientSocket);
        }
    }

    private void handleRequest(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true)
        ) {
            String inputLine = in.readLine();
            if (inputLine == null) return;
            System.out.println("Solicitud: " + inputLine);

            String[] requestParts = inputLine.split(" ");
            String path = requestParts[1];
            HttpRequest req = new HttpRequest(path);
            HttpResponse res = new HttpResponse();

            if (getRoutes.containsKey(req.getPath())) {
                String result = getRoutes.get(req.getPath()).handle(req, res);
                sendResponse(writer, res.getType(), result);
            } else {
                if (path.equals("/")) path = "/index.html";
                File file = new File(staticFolder + path);
                if (file.exists() && !file.isDirectory()) {
                    String mimeType = Files.probeContentType(file.toPath());
                    if (mimeType == null) {
                        if (file.getName().endsWith(".css")) mimeType = "text/css";
                        else if (file.getName().endsWith(".js")) mimeType = "application/javascript";
                        else mimeType = "text/plain";
                    }
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    sendFileResponse(out, writer, mimeType, fileContent);
                } else {
                    String notFound = "<h1>404 Not Found</h1>";
                    sendResponse(writer, "text/html", notFound);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(PrintWriter writer, String contentType, String response) {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + contentType + "; charset=utf-8");
        writer.println("Content-Length: " + response.getBytes(StandardCharsets.UTF_8).length);
        writer.println();
        writer.println(response);
    }

    private void sendFileResponse(OutputStream out, PrintWriter writer, String mimeType, byte[] fileContent) throws IOException {
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + mimeType);
        writer.println("Content-Length: " + fileContent.length);
        writer.println();
        writer.flush();
        out.write(fileContent);
        out.flush();
    }


    // Método para iniciar el servidor y registrar controladores

    public static void runServer() throws IOException {
        HttpServer server = new HttpServer();

        // Configurar carpeta de archivos estáticos
        server.staticfiles("webroot/public");

        // Paquete donde buscar los controladores
        String basePackage = "co.escuelaing.arep.microspringboot.examples";

        try {
            // Carpeta física donde están las clases compiladas
            File folder = new File("target/classes/" + basePackage.replace(".", "/"));

            if (folder.exists() && folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    if (file.getName().endsWith(".class")) {
                        // Nombre completo de la clase
                        String className = basePackage + "." + file.getName().replace(".class", "");
                        Class<?> clazz = Class.forName(className);

                        // Revisar si está anotada con @RestController
                        if (clazz.isAnnotationPresent(
                                co.escuelaing.arep.microspringboot.annotations.RestController.class)) {

                            Object controller = clazz.getDeclaredConstructor().newInstance();

                            for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                                if (method.isAnnotationPresent(
                                        co.escuelaing.arep.microspringboot.annotations.GetMapping.class)) {

                                    String path = method.getAnnotation(
                                        co.escuelaing.arep.microspringboot.annotations.GetMapping.class
                                    ).value();

                                    server.get(path, (req, res) -> {
                                        try {
                                            // Resolver parámetros
                                            Object[] params = new Object[method.getParameterCount()];
                                            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                                            Class<?>[] parameterTypes = method.getParameterTypes();

                                            for (int i = 0; i < parameterTypes.length; i++) {
                                                if (parameterTypes[i] == String.class) {
                                                    String value = null;
                                                    for (Annotation ann : parameterAnnotations[i]) {
                                                        if (ann instanceof co.escuelaing.arep.microspringboot.annotations.RequestParam) {
                                                            co.escuelaing.arep.microspringboot.annotations.RequestParam rp =
                                                                (co.escuelaing.arep.microspringboot.annotations.RequestParam) ann;
                                                            value = req.getQueryParam(rp.value());
                                                            if (value == null) value = rp.defaultValue();
                                                        }
                                                    }
                                                    params[i] = value;
                                                }
                                            }

                                            Object result = method.invoke(controller, params);
                                            return result.toString();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return "500 Internal Server Error";
                                        }
                                    });

                                    System.out.println("Ruta registrada: " + path + " -> " + method.getName());
                                }
                            }
                        }
                    }
                }
            } else {
                System.err.println("No se encontró la carpeta: " + folder.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.start(8080);
    }


}
