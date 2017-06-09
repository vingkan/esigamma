import java.lang.*;
import java.text.*;
import java.io.*;
import java.util.*;
import com.google.firebase.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.*;
import java.util.concurrent.CompletableFuture.*;
import java.util.concurrent.ExecutorService.*;
import java.util.concurrent.Executors.*;

class FireWorker {
    
    public FireWorker() {
        Thread thread = new Thread(new FirebaseRunner(){
            @Override
            public void workerOperation(AtomicBoolean done, CompletionListener cl){
                operation(done, cl);
            }
            @Override
            public void workerCallback(DatabaseError de, DatabaseReference dr){
                callback(de, dr);
            }
        });
        thread.start();
    }
    
    public void operation(AtomicBoolean done, CompletionListener cl){
        if(cl == null){
            done.set(true);
        }
    }
    
    public void callback(DatabaseError de, DatabaseReference dr){
        System.out.println("\nDo nothing.\n");
    }
    
    class FirebaseRunner implements Runnable {
    
        @Override
        public void run() {
            AtomicBoolean done = new AtomicBoolean(false);
            workerOperation(done, new CompletionListener(){
                @Override
                public void onComplete(DatabaseError de, DatabaseReference dr){
                    workerCallback(de, dr);
                    done.set(true);
                }
            });
            while(!done.get());
        }
        
        public void workerOperation(AtomicBoolean done, CompletionListener cl){
            operation(done, cl);
        }
        
        public void workerCallback(DatabaseError de, DatabaseReference dr){
            callback(de, dr);
        }
        
    }
    
}