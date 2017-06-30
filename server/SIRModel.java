import java.util.*;
import java.io.*;

/*
 * Server-Side SIR Model
   The SIR model is one of most common methods for modeling the spread of
   infectious diseases. Though it is described in terms of differential
   equations, it can be implemented relatively simply as a computer program.
   
 * Instructions for Testing
   Edit the simulation constants to your liking.
   >> javac SIRModel.java
   >> java SIRModel
   Results will be in the specified OUTFILE, graph them in a spreadsheet.
 */

class SIRModel {
    
    // Simulation Constants
    
    private static double BETA = 0.9;               // % of S -> I
    private static double GAMMA = 0.2;              // % of I -> R
    
    private static double S0 = 950;                    // Initially Susceptible
    private static double I0 = 50;                     // Initially Infected
    private static double R0 = 0;                      // Initially Resistant
    private static double N = S0 + I0 + R0;            // Total Population
    
    private static double[] RANGE = {0.0, 10.0};    // Simulation Range in Days
    //private static double DT = 0.1;                 // Step Size in Days
    private static double DT = 0.1;                 // Step Size in Days
    
    // Data Structure for storing records
    public static List<Double> S_HIST = new ArrayList<Double>();
    public static List<Double> I_HIST = new ArrayList<Double>();
    public static List<Double> R_HIST = new ArrayList<Double>();
    
    private static String OUTFILE = "sir-output.txt";   // Output File
    
    public SIRModel(double beta, double gamma, int s0, int i0, int r0, double days) {
        S_HIST.clear();
        I_HIST.clear();
        R_HIST.clear();
        this.BETA = beta;
        this.GAMMA = gamma;
        this.S0 = (double)s0;
        this.I0 = (double)i0;
        this.R0 = (double)r0;
        this.RANGE[1] = days;
        S_HIST.add(S0);
        I_HIST.add(I0);
        R_HIST.add(R0);
    }
    
    public SIRModel(double beta, double gamma, int s0, int i0, int r0, double dt, double days) {
        S_HIST.clear();
        I_HIST.clear();
        R_HIST.clear();
        this.BETA = beta;
        this.GAMMA = gamma;
        this.S0 = (double)s0;
        this.I0 = (double)i0;
        this.R0 = (double)r0;
        this.DT = dt;
        this.RANGE[1] = days;
        S_HIST.add(S0);
        I_HIST.add(I0);
        R_HIST.add(R0);
    }
    
    public static void main(String[] args) {
        
        // Initialize Simulation
        S_HIST.add(S0);
        I_HIST.add(I0);
        R_HIST.add(R0);
        
        // Run Simulation
        double Sf = S(RANGE[1]);
        double If = I(RANGE[1]);
        double Rf = R(RANGE[1]);
        
        // TODO: Check that records have been correctly populated
        
        // Output Results
        try{
        
            FileWriter fw = new FileWriter(OUTFILE);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (double ti = RANGE[0]; ti <= RANGE[1]; ti += DT) {
                double Si = getRecord(S_HIST, ti);
                double Ii = getRecord(I_HIST, ti);
                double Ri = getRecord(R_HIST, ti);
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
    
    public static void simulate() {
        double Sf = S(RANGE[1]);
        double If = I(RANGE[1]);
        double Rf = R(RANGE[1]);
    }
    
    /*
     * @param t - simulation time in days
     * @return - number of people susceptible at time t
     */
    public static double S(double t) {
        double count = getRecord(S_HIST, t);
        if (count >= 0) {
            return count;
        }
        else {
            double lt = t - DT;
            double newCount = ( (S(lt)) + dS(t) );
            S_HIST.add(newCount);
            return newCount;
        }
    }

    /*
     * @param t - simulation time in days
     * @return - number of people infected at time t
     */    
    public static double I(double t) {
        double count = getRecord(I_HIST, t);
        if (count >= 0) {
            return count;
        }
        else {
            double lt = t - DT;
            double newCount = ( (I(lt)) + dI(t) );
            I_HIST.add(newCount);
            return newCount;
        }
    }

    /*
     * @param t - simulation time in days
     * @return - number of people resistant at time t
     */    
    public static double R(double t) {
        double count = getRecord(R_HIST, t);
        if (count >= 0) {
            return count;
        }
        else {
            double lt = t - DT;
            double newCount = ( (R(lt)) + dR(t) );
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
        double change = ( -1.0 * BETA * ( ( (S(lt)) * (I(lt)) ) / (N) ) );
        return change * DT;
    }

    /*
     * @param t - simulation time in days
     * @return - rate of change in infected population at time t
     */     
    public static double dI(double t) {
        double lt = t - DT;
        double change = ( BETA * ( ( (S(lt)) * (I(lt)) ) / (N) ) );
        double removed = ( -1.0 * GAMMA * (I(lt)) );
        return (change + removed) * DT;
    }

    /*
     * @param t - simulation time in days
     * @return - rate of change in resistant population at time t
     */     
    public static double dR(double t) {
        double lt = t - DT;
        double change = ( GAMMA * (I(lt)) );
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
    private static double getRecord(List<Double> records, double t) {
        int index = getTimeIndex(t);
        try {
            double count = records.get(index);
            return count;
        }
        catch(Exception e){
            return -1.0;
        }
    }
    
    public static List<Double> getTimeRecords() {
        List<Double> timeRecords = new ArrayList<Double>();
        for (double ti = RANGE[0]; ti <= RANGE[1]; ti += DT) {
            timeRecords.add(ti);
        }
        return timeRecords;
    }
    
}