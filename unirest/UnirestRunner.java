import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;

/*
 * http://blog.mashape.com/installing-unirest-java-with-the-maven-assembly-plugin/
 * https://stackoverflow.com/questions/23630681/how-to-parse-json-results-from-unirest-call?rq=1
 * https://www.vainolo.com/2015/10/14/calling-a-web-api-from-java-using-unirest/
 */
 
/*
 * Instructions
 * $ javac -cp ".:lib/*" UnirestRunner.java
 * $ java -cp ".:lib/*" UnirestRunner
 */

public class UnirestRunner {

    public static void main(String[] args) {
        
        try {
            foodInspectionDemo(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private static double METERS_PER_MILE = 1609.34;
    private static double MS_PER_DAY = 100 * 60 * 60 * 24;
    
    /*
     * CLI Usage:
     * $ javac -cp ".:lib/*" *.java
     * $ java -cp ".:lib/*" UnirestRunner filename limit lat lng radius (in miles)
     * $ java -cp ".:lib/*" UnirestRunner inspections.txt 50 41.8350991 -87.6276149 5
     */
    public static void foodInspectionDemo(String[] args) throws Exception {
        if (args.length != 5) {
            throw new Exception("Impromper Arguments.");
        }
        String filename = args[0];
        int limit = Integer.parseInt(args[1]);
        /*
         * Search Locations within Radius with Socrata Query:
         * '$where': 'within_circle(location, 40.15, -88.25, 1000)'
         */
        double cLat = Double.parseDouble(args[2]);
        double cLng = Double.parseDouble(args[3]);
        double radius = Double.parseDouble(args[4]) * METERS_PER_MILE;
        String radiusQuery = String.format("within_circle(location,%s,%s,%s)", cLat, cLng, radius);
        String URL = "https://data.cityofchicago.org/resource/cwig-ma7x.json";
            HttpResponse<String> request = Unirest.get(URL)
                .queryString("$limit", limit)
                .queryString("$where", radiusQuery)
                .queryString("$order", "inspection_date DESC")
                //.queryString("results", "Pass")
                .asString();
            JSONArray results = new JSONArray(request.getBody());
            for (int i = 0; i < results.length(); i++) {
                try {
                    JSONObject res = (JSONObject) results.getJSONObject(i);
                    //System.out.println(res);
                    String license = res.get("license_").toString();
                    String name = res.get("dba_name").toString();
                        name = cleanStringForCSV(name);
                    String risk = res.get("risk").toString();
                    String lat = res.get("latitude").toString();
                    String lng = res.get("longitude").toString();
                    String dateStr = res.get("inspection_date").toString();
                    int days = 0;
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        Date inspectDate = df.parse(dateStr);
                        Date now = new Date();
                        double diff = (double) now.getTime() - (double) inspectDate.getTime();
                        double daysSince = diff / MS_PER_DAY;
                        days = (int) Math.round(daysSince);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        throw new Exception("Could not parse date of inspection.");
                    }
                    String inspectionResult = res.get("results").toString();
                    String highRisk;
                    if (risk.equals("Risk 1 (High)")) {
                        highRisk = "true";
                    } else {
                        highRisk = "false";
                    }
                    String pass;
                    if (inspectionResult.equals("Pass")) {
                        pass = "true";
                    } else if (inspectionResult.equals("Fail")) {
                        pass = "false";
                    } else {
                        throw new Exception("Special case for food inspection results.");
                    }
                    String violations; 
                    try {
                        violations = res.get("violations").toString();
                        violations = cleanStringForCSV(violations);
                    } catch (Exception e) {
                        violations = "None.";
                    }
                    String out = String.format("%s,%s,%s,%s,%s,%s,%s,%s", lat, lng, name, license, pass, highRisk, days, violations);
                    //System.out.println(out);
                    Helper.writeFileLine(filename, out);
                } catch (Exception e) {
                    // Ignore
                }
            }
        Helper.closeAllFiles();
    }
    
    public static String cleanStringForCSV(String input) {
        String output = input;
        output = output.replaceAll(Pattern.quote(","), ".");
        output = output.replaceAll("\n", " ");
        return output;
    }

    public static void randomDemo(String[] args) {
        
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
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
    
}