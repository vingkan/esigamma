import java.util.*;
import java.awt.Color;

public class Atlas {
    
    public static void main(String[] args) {
        
        String markersFile = "markers.txt";
        String coordsFile = "coords.txt";
        
        Atlas atlas = new Atlas();
        List<Marker> markers = new ArrayList<Marker>();
        List<String[]> coords = Helper.readCoordsFromFile(coordsFile);
        
        for (String[] location : coords) {
            
            double lat = Double.parseDouble(location[0]);
            double lng = Double.parseDouble(location[1]);
            String name = location[2];
            Color color = Color.BLUE;
            double size = 10;
            double opacity = 0.5;
            
            boolean isRestaurant = name.contains("(R)");
            if (isRestaurant) {
                color = Color.RED;
            }
            
            Marker marker = new Marker(lat, lng, color, size, opacity, name);
            markers.add(marker);
            
        }
        
        atlas.writeAtlasData(markersFile, markers);
        Helper.closeAllFiles();
        
    }
    
    public Atlas() {
        
    }
    
    public void writeAtlasData(String filename, List<Marker> markers) {
        for (Marker marker : markers) {
            String data = marker.toData() + "";
            Helper.writeFileLine(filename, data);
        }
    }
    
}