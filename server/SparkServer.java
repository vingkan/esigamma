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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference.*;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.*;
import java.util.concurrent.CompletableFuture.*;
import java.util.concurrent.ExecutorService.*;
import java.util.concurrent.Executors.*;

/*
 * RUN
 * >> javac -cp ".:lib/*" SparkServer.java
 * >> java -cp ".:lib/*" SparkServer
 * Open: http://esigamma-vingkan.c9users.io/{route}
 */

public class SparkServer {

    public static void main(String[] args){
        
        /*class CodeRunner implements Runnable {
            @Override
            public void run() {
                AtomicBoolean done = new AtomicBoolean(false);
                final boolean running = true;
                System.out.println("Running");
                FirebaseDatabase db = initDatabase();
                DatabaseReference logRef = db.getReference("logs");
                
                logRef.setValue("Haosheng", new CompletionListener(){
                    @Override
                    public void onComplete(DatabaseError de, DatabaseReference dr){
                        System.out.println(de);
                        System.out.println(dr);
                        done.set(true);
                    }
                });
                while(!done.get());
                System.out.println("Done Running");
            }
        }*/
        
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
        
        /*final FirebaseDatabase db = initDatabase();
        FireWorker fw = new FireWorker(){
            @Override
            public void operation(AtomicBoolean done, CompletionListener cl){
                System.out.println("\nRun this now.\n");
                DatabaseReference logRef = db.getReference("logs");
                    DateFormat format = new SimpleDateFormat("hh:mm:ss a");
                    Date date = new Date();
                    String dStr = format.format(date);
                logRef.setValue(dStr, cl);
                System.out.println(done);
            }
            @Override
            public void callback(DatabaseError de, DatabaseReference dr){
                System.out.println("\nAll I wanted.\n");
                System.out.println(de);
                System.out.println(dr);
            }
        };*/
        /*FireWorker fw2 = new FireWorker(){
            @Override
            public void operation(AtomicBoolean done, CompletionListener cl){
                System.out.println("\nRun thread 2.\n");
                DatabaseReference ref = db.getReference("times");
                    DateFormat format = new SimpleDateFormat("hh:mm:ss a");
                    Date date = new Date();
                    String dStr = format.format(date);
                ref.setValue(dStr, cl);
                System.out.println(done);
            }
            @Override
            public void callback(DatabaseError de, DatabaseReference dr){
                System.out.println("\nIt has been done.\n");
                System.out.println(de);
                System.out.println(dr);
            }
        };*/
        
        /*FirebaseDatabase db = initDatabase();
        DatabaseReference logRef = db.getReference("logs");*/
        
        /*logRef.setValue("Zika", new CompletionListener(){
            @Override
            public void onComplete(DatabaseError de, DatabaseReference dr){
                System.out.println(de);
                System.out.println(dr);
            }
        });*/
        
        /*logRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snap){
                Object r = snap.getValue();
                System.out.println(r);
            }
            @Override
            public void onCancelled(DatabaseError in){
                System.out.println(in);
            }
        });
        
        try{
            Thread.sleep(20000);
        }
        catch(Exception te){
            System.out.println(te);
        }*/
        
        /*final AtomicBoolean done = new AtomicBoolean(false);
        DatabaseReference logRef = db.getReference("logs");
        //DatabaseReference pushed = logRef.push();
        logRef.setValue("esigamma", new CompletionListener(){
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref){
                if(error != null){
                    System.out.println("Error: " + error.getMessage());
                }
                else{
                    System.out.println("Completed");
                    done.set(true);
                }
            }
        });
        logRef.setValue("esigamma");
        System.out.println("Here");
        while(!done.get());*/
        
            
        get("/", (req, res) -> {
           return "ESI CS Gamma Spark Server"; 
        });
        
        get("/hello", (req, res) -> "Hello World!");
        
        
        /*
         * Example of an asynchronous response
         * http://www.baeldung.com/java-completablefuture
         * http://codingjunkie.net/completable-futures-part1/
         */
        get("/async", (req, res) -> {
            CompletableFuture<String> cf = new CompletableFuture<>();
            ExecutorService svc = Executors.newCachedThreadPool();
            svc.submit(() -> {
                Thread.sleep(2000);
                cf.complete("Async Hello!");
                return null;
            });
            //return cf.get();
            return "Zika";
        });
        
        /*
         * Example of reflecting input parameters in response
         */
        get("/self/:name", (req, res) -> {
            JSONObject obj = new JSONObject();
            // Fetch a parameter from the URL
            obj.put("name", req.params(":name"));
            // Fetch a parameter from the query
            obj.put("data", req.queryParams("data"));
            return obj;
        });
        
        /*
         * Example of using local classes to generate response
         */
        get("/rover", (req, res) -> {
            String name = req.queryParams("name");
            if(name == null){
                name = "Unnamed Rover";
            }
            Rover rover = new Rover(name); 
            JSONObject obj = new JSONObject();
            obj.put("name", rover.getName());
            return obj;
        });
        
        /*post("/measurements", (req, res) -> {
            double data = Double.parseDouble(req.queryParams("data"));
            CompletableFuture<String> cf = new CompletableFuture<>();
            FireWorker fw2 = new FireWorker(){
                @Override
                public void operation(AtomicBoolean done, CompletionListener cl){
                    DatabaseReference ref = db.getReference("measurements");
                    ref.setValue(data, cl);
                }
                @Override
                public void callback(DatabaseError de, DatabaseReference dr){
                    cf.complete(dr.getKey());
                }
            };

            JSONObject obj = new JSONObject();
            obj.put("key", cf.get());
            return obj;
        });*/
        
        System.out.println("\nEnd.\n");
    
    }
    
    public static FirebaseDatabase initDatabase(){
        FileInputStream serviceAccount = null;
        try{
            serviceAccount = new FileInputStream("serviceAccountKey.json");
        }
        catch(IOException e){
            System.out.println(e);
        }
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
            .setDatabaseUrl("https://esigamma.firebaseio.com")
            .build();
        FirebaseApp.initializeApp(options);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        return db;
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