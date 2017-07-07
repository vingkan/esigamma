import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.*;
import java.util.*;

/*
 * http://blog.mashape.com/installing-unirest-java-with-the-maven-assembly-plugin/
 * https://stackoverflow.com/questions/23630681/how-to-parse-json-results-from-unirest-call?rq=1
 * https://www.vainolo.com/2015/10/14/calling-a-web-api-from-java-using-unirest/
 */

public class UnirestRunner {

    public static void main(String[] args) {
        
        System.out.println("GET Random American Names...");
        
        try {
            
            int n = Integer.parseInt(args[0]);
            String URL = "https://randomuser.me/api/";
            HttpResponse<String> request = Unirest.get(URL)
                .queryString("nat", "us")
                .queryString("results", n)
                .asString();
            JSONObject response = new JSONObject(request.getBody());
            JSONArray results = (JSONArray) response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject res = (JSONObject) results.getJSONObject(i);
                JSONObject nameObj = (JSONObject) res.get("name");
                String first = (String) nameObj.get("first");
                String last = (String) nameObj.get("last");
                System.out.println((i + 1) + ". " + toSentenceCase(first) + " " + toSentenceCase(last));
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
        
    }
    
    public static String toSentenceCase(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    
}