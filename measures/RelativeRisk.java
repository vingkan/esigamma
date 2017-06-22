/*
 * Date: ???
 * Author: ???
 * This program calculates relative risk.
 * To run, type the following in your terminal:
   > javac RelativeRisk.java
   > java RelativeRisk
 */

public class RelativeRisk {
    
    /*
     * This code runs when the program is executed.
     */
    public static void main(String[] args){
        
        demonstrateDivision();
        
    }
    
    /*
     * This is a demonstration... you can remove it later.
     * Input: None
     * Output: Prints division answers to terminal
     */
    public static void demonstrateDivision(){
        double divideInteger = divide(3, 7);
        double divideDouble = divide(3.0, 7.0);
        System.out.println("Integer Answer: " + divideInteger);
        System.out.println("Double Answer: " + divideDouble);
    }
    
    /*
     * Divides two integers.
     * Input: int a: numerator, int b: denominator
     * Output: double, quotient
     */
    public static double divide(int a, int b){
        double answer = a / b;
        return answer;
    }
    
    /*
     * Divides two doubles.
     * Input: doubles a: numerator, doubles b: denominator
     * Output: double, quotient
     */
    public static double divide(double a, double b){
        double answer = a / b;
        return answer;
    }
    
}