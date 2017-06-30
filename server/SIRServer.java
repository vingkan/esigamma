/*
 * REFERENCES
 * http://sparkjava.com/documentation#getting-started
 * http://www.programcreek.com/2014/01/compile-and-run-java-in-command-line-with-external-jars/
 */
 
import static spark.Spark.*;
import org.json.simple.JSONObject;

import java.lang.*;
import java.text.*;
import java.io.*;
import java.util.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CompletableFuture.*;
import java.util.concurrent.ExecutorService.*;
import java.util.concurrent.Executors.*;

/*
 * SIR Model Server
   Java Spark Server to compute SIR model records from initial values.
   
 * RUN
   >> javac -cp ".:lib/*" SIRServer.java
   >> java -cp ".:lib/*" SIRServer
   Open: http://esigamma-vingkan.c9users.io/{route}
 */

public class SIRServer {

    public static void main(String[] args){
        
        // Cloud9 environments serve localhost to port 8080
        port(8080);
        // Enable Cross-Origin Requests
        enableCORS("*", "*", "*");
        
        before((req, res) -> {
            String path = req.pathInfo();
            if(path.endsWith("/") && path.length() > 1){
                res.redirect(path.substring(0, path.length() - 1));
            }
        });
        
        System.out.println("\nStarted.\n");
            
        get("/", (req, res) -> {
           return "ESI CS SIR Model Server"; 
        });
        
        /*
         * Example Query URL
         * http://esigamma-vingkan.c9users.io/sir?b=0.9&g=0.1&s=950&i=50&r=0
         */
        get("/sir", (req, res) -> {
            double beta = Double.parseDouble(req.queryParams("b"));
            double gamma = Double.parseDouble(req.queryParams("g"));
            int s0 = Integer.parseInt(req.queryParams("s"));
            int i0 = Integer.parseInt(req.queryParams("i"));
            int r0 = Integer.parseInt(req.queryParams("r"));
            double days = Double.parseDouble(req.queryParams("d"));
            SIRModel model = new SIRModel(beta, gamma, s0, i0, r0, days);
            model.simulate();
            JSONObject obj = new JSONObject();
            obj.put("t", model.getTimeRecords());
            obj.put("s", model.S_HIST);
            obj.put("i", model.I_HIST);
            obj.put("r", model.R_HIST);
            return obj;
        });
        
        System.out.println("\nEnd.\n");
    
    }
    
    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {
    
        options("/*", (request, response) -> {
    
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
    
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
    
            return "OK";
        });
    
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
    
}