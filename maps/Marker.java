import java.awt.Color;

public class Marker {
    
    private double lat;
    private double lng;
    private Color color;
    private double size;
    private double opacity;
    private String text;
    
    public Marker() {
        this.lat = 0.0;
        this.lng = 0.0;
        this.color = Color.BLACK;
        this.size = 1;
        this.opacity = 1.0;
        this.text = "";
    }
    
    public Marker(double lat, double lng, Color color, double size, double opac, String text) {
        this.lat = lat;
        this.lng = lng;
        this.color = color;
        this.size = size;
        this.opacity = opac;
        this.text = text;
    }
    
    public Marker(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        this.color = Color.BLACK;
        this.size = 1;
        this.opacity = 1.0;
        this.text = "";
    }
    
    public double getLat() {
        return this.lat;
    }
    
    public double getLng() {
        return this.lng;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public double getSize() {
        return this.size;
    }
    
    public double getOpacity() {
        return this.opacity;
    }
    
    public String getText() {
        return this.text;
    }
    
    public String toData() {
        double lat = this.getLat();
        double lng = this.getLng();
        Color color = this.getColor();
        double size = this.getSize();
        double opac = this.getOpacity();
        String text = this.getText();
        String r = color.getRed() + "";
        String g = color.getGreen() + "";
        String b = color.getBlue() + "";
        String colorStr = "rgb(" + String.join(", ", r, g, b) + ")";
        String out = String.format("%s$@$%s$@$%s$@$%s$@$%s$@$%s$@@$", lat, lng, colorStr, size, opac, text);
        return out;
    }
    
}