import java.awt.Color;

public class Plotter {
    
    public static main(String[] args) {
        
        
        new Marker()
        
        plotter.addToPlot()
        
    }
    
    public Plotter() {
        
    }
    
    public class Marker {
        
        public Marker(double lat, double lng, Color color, double size, double opac, String text) {
            
        }
        
        public String toString() {
            double lat = this.getLat();
            double lng = this.getLng();
            Color color = this.getColor();
            double size = this.getSize();
            double opac = this.getOpacity();
            String text = this.getText();
            String out = String.format("%s, %s, %s, %s, %s, %s", lat, lng, color, size, opac, text);
            return out;
        }
        
        
    }
    
    
    
    
}