/*
 * Java SDK Installation for Cloud9
 * http://stackoverflow.com/questions/36445901/installing-java-8-on-cloud9
 * http://stackoverflow.com/questions/28196434/setting-up-cloud9-ide-to-compile-and-run-javaj
 * Master Write for vim: :w !sudo tee % > /dev/null
 */

class SampleMain {
    
    /*
     * INSTRUCTIONS
     * Compile: javac SampleMain.java
     * Run: java SampleMain x1 y1 x2 y2
     * Output: Coordinates of the midpoint like so:
     * >> x: 3, y: 4
     */
    
    public static void main(String[] args){
        
        int[] p1 = {0, 0};
        int[] p2 = {3, 0};
        
        int[] midPoint = getMidPoint(p1, p2);
        
        System.out.println("x: " + midPoint[0] + ", y: " + midPoint[1]);
        
    }
    
    public static int[] getMidPoint(int[] p1, int[] p2){
        int[] mp = {0, 0};
        int x = (p2[0] - p1[0]) / 2;
        int y = (p2[1] - p1[1]) / 2;
        return mp;
    }
    
}