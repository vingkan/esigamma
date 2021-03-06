import java.util.*;
import java.io.*;

/*
 * Simple SIR Simulation Implementation in Java
   The SIR model is one of most common methods for modeling the spread of
   infectious diseases. Though it is described in terms of differential
   equations, it can be implemented relatively simply as a computer program.
   
 * Instructions for Running
   Edit the simulation constants to your liking.
   >> javac SIRSimulator.java
   >> java SIRSimulator
   Results will be in the specified OUTFILE, graph them in a spreadsheet.
   
 * Discussion Questions
   Experiment with 0.5, 0.25, and 0.10 as values of DT. What happens when 0.01
   is used as the value of DT? Why?
   How might this program be rewritten so it can be used as a component for a
   larger Java program?
   
 * Links for Futher Reading
   http://www.maa.org/press/periodicals/loci/joma/the-sir-model-for-spread-of
   -disease-the-differential-equation-model
   http://www.public.asu.edu/~hnesse/classes/sir.html
 */

class SIRSimulator {
    
    // Simulation Constants
    
    private static double BETA = 0.9;               // % of S -> I
    private static double GAMMA = 0.2;              // % of I -> R
    
    private static int S0 = 950;                    // Initially Susceptible
    private static int I0 = 50;                     // Initially Infected
    private static int R0 = 0;                      // Initially Resistant
    private static int N = S0 + I0 + R0;            // Total Population
    
    private static double[] RANGE = {0.0, 10.0};    // Simulation Range in Days
    private static double DT = 0.1;                 // Step Size in Days
    
    // Data Structure for storing records
    private static List<Integer> S_HIST = new ArrayList<Integer>();
    private static List<Integer> I_HIST = new ArrayList<Integer>();
    private static List<Integer> R_HIST = new ArrayList<Integer>();
    
    private static String OUTFILE = "sir-output.txt";   // Output File
    
    public static void main(String[] args) {
        
        // Initialize Simulation
        S_HIST.add(S0);
        I_HIST.add(I0);
        R_HIST.add(R0);
        
        // Run Simulation
        int Sf = S(RANGE[1]);
        int If = I(RANGE[1]);
        int Rf = R(RANGE[1]);
        
        // TODO: Check that records have been correctly populated
        
        // Output Results
        try{
        
            FileWriter fw = new FileWriter(OUTFILE);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (double ti = RANGE[0]; ti <= RANGE[1]; ti += DT) {
                int Si = getRecord(S_HIST, ti);
                int Ii = getRecord(I_HIST, ti);
                int Ri = getRecord(R_HIST, ti);
                String row = ti + "\t" + Si+ "\t" + Ii + "\t" + Ri + "\n";
                bw.write(row);
            }
            
            bw.close();
            fw.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /*
     * @param t - simulation time in days
     * @return - number of people susceptible at time t
     */
    public static int S(double t) {
        int count = getRecord(S_HIST, t);
        if (count != -1) {
            return count;
        }
        else {
            double lt = t - DT;
            int newCount = (int) ( S(lt) + dS(t) );
            S_HIST.add(newCount);
            return newCount;
        }
    }

    /*
     * @param t - simulation time in days
     * @return - number of people infected at time t
     */    
    public static int I(double t) {
        int count = getRecord(I_HIST, t);
        if (count != -1) {
            return count;
        }
        else {
            double lt = t - DT;
            int newCount = (int) ( I(lt) + dI(t) );
            I_HIST.add(newCount);
            return newCount;
        }
    }

    /*
     * @param t - simulation time in days
     * @return - number of people resistant at time t
     */    
    public static int R(double t) {
        int count = getRecord(R_HIST, t);
        if (count != -1) {
            return count;
        }
        else {
            double lt = t - DT;
            int newCount = (int) ( R(lt) + dR(t) );
            R_HIST.add(newCount);
            return newCount;
        }
    }

    /*
     * @param t - simulation time in days
     * @return - rate of change in susceptible population at time t
     */    
    public static double dS(double t) {
        double lt = t - DT;
        double change = (double) ( -1 * BETA * ( ( S(lt) * I(lt) ) / N ) );
        return change * DT;
    }

    /*
     * @param t - simulation time in days
     * @return - rate of change in infected population at time t
     */     
    public static double dI(double t) {
        double lt = t - DT;
        double change = (double) ( BETA * ( ( S(lt) * I(lt) ) / N ) );
        double removed = (double) ( -1 * GAMMA * I(lt) );
        return (change + removed) * DT;
    }

    /*
     * @param t - simulation time in days
     * @return - rate of change in resistant population at time t
     */     
    public static double dR(double t) {
        double lt = t - DT;
        double change = (double) ( GAMMA * I(lt) );
        return change * DT;
    }
    

    /*
     * @param t - simulation time in days
     * @return - index in data structure that corresponds to time t
     */     
    private static int getTimeIndex(double t) {
        double passed = t - RANGE[0];
        int index = (int) ( passed / DT );
        if (index < 0) {
            throw new Error("Invalid time index.");
        }
        return index;
    }
 
    /*
     * @param records - data structure of records to search
     * @param t - simulation time in days
     * @return - number of people recorded in data structure at time t
     */    
    private static int getRecord(List<Integer> records, double t) {
        int index = getTimeIndex(t);
        try {
            int count = records.get(index);
            return count;
        }
        catch(Exception e){
            return -1;
        }
    }
    
}