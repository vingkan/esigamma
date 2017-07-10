import java.util.*;
import java.io.*;
import java.util.regex.*;

public class Helper {

    private static boolean isSeeded = false;
    private static Random random = new Random();
    public static Random getRandom() {
        if(!isSeeded){
            Helper.printlnLimitTo("no_seed", "Warning: No seed, values not reproducible.", 1);
        }
        return random;
    }
    public static void setSeed(int seed){
        isSeeded = true;
        random.setSeed(seed);
    }
    
    private static Map<String, Integer> printRecords = new HashMap<String, Integer>();
    public static void printlnLimitTo(String key, String content, int limit){
        if(!printRecords.containsKey(key)){
            printRecords.put(key, 0);
        }
        if(printRecords.get(key) < limit){
            System.out.println(content);
            int count = printRecords.get(key);
                count++;
            printRecords.put(key, count);
        }
    }
    
    private static class FileRecord {
        
        private String filename;
        private FileWriter fw = null;
        private BufferedWriter bw = null;
        private FileReader fr = null;
        private BufferedReader br = null;
        
        public FileRecord(String filename){
            this.filename = filename;
        }
        
        public boolean isOpenForWriting(){
            return this.bw != null;
        }
        
        public boolean isOpenForReading(){
            return this.br != null;
        }
        
        public void openForWriting(){
            try{
                this.fw = new FileWriter(filename);
                this.bw = new BufferedWriter(this.fw);
            }
            catch(IOException e){
                System.out.println("Error in openForWriting()");
                e.printStackTrace();
            }
        }
        
        public void openForReading(){
            try{
                this.fr = new FileReader(filename);
                this.br = new BufferedReader(this.fr);
            }
            catch(IOException e){
                System.out.println("Error in openForReading()");
                e.printStackTrace();
            }
        }
        
        public void writeFileLine(String content){
            try{
                bw.write(content + "\n");
            }
            catch(IOException e){
                System.out.println("Error in writeFileLine()");
                e.printStackTrace();
            }
        }
        
        public String readEntireFile(){
            String out = "";
            StringBuilder sb = new StringBuilder();
            try{
                String line = null;
                boolean reading = true;
                while(reading){
                    line = br.readLine();
                    if(line == null){
                        reading = false;
                    }
                    else{
                        //out += line;
                        sb.append(line);
                    }
                }
                out = sb.toString();
            }
            catch(IOException e){
                System.out.println("Error in readEntireFile()");
                e.printStackTrace();
            }
            return out;
        }
        
        public void closeFile(){
            try{
                if(bw != null){
                    bw.close();
                }
                if(fw != null){
                    fw.close();
                }
                if(br != null){
                    br.close();
                }
                if(fr != null){
                    fr.close();
                }
            }
            catch(IOException e){
                System.out.println("Error in closeFile()");
                e.printStackTrace();
            }
        }
        
    }
    
    private static boolean allowWrites = true;
    private static HashMap<String, FileRecord> fileMap = new HashMap<String, FileRecord>();
    
    public static void writeFileLine(String filename, String content){
        if(allowWrites){
            if(!fileMap.containsKey(filename)){
                fileMap.put(filename, new FileRecord(filename));
            }
            FileRecord rec = fileMap.get(filename);
            if(!rec.isOpenForWriting()){
               rec.openForWriting(); 
            }
            rec.writeFileLine(content);
        }
    }
    
    public static String readEntireFile(String filename){
        if(!fileMap.containsKey(filename)){
            fileMap.put(filename, new FileRecord(filename));
        }
        FileRecord rec = fileMap.get(filename);
        if(!rec.isOpenForReading()){
            rec.openForReading();
        }
        String content = rec.readEntireFile();
        return content;
    }
    
    public static void closeAllFiles(){
        for(Map.Entry<String, FileRecord> entry : fileMap.entrySet()){
            entry.getValue().closeFile();
        }
    }

    public static void disableWrites(){
        allowWrites = false;
    }
    
    public static void enableWrites(){
        allowWrites = true;
    }

    public static List<String[]> readCoordsFromFile(String filename){
        List<String[]> coords = new ArrayList<String[]>();
        String input = Helper.readEntireFile(filename);
        String[] coordStrs = input.split(Pattern.quote("$"));
        for(String str : coordStrs){
            String[] pair = str.split(Pattern.quote(","));
            coords.add(pair);
        }
        return coords;
    }

}