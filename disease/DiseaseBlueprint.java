
abstract class DiseaseBlueprint {
    
    abstract DiseaseAction move(SimulatedHost host, int energy);
    
    public DiseaseBlueprint() {
        
    }

    public String getName() {
        return "[DEFAULT] Disease Name";
    }
    
    public double getInfectivity(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public double getToxigenicity(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public double getResistance(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public void printSummary() {
        System.out.println("Summary for " + this.getName());
        String header = "Age\tI\tT\tR";
        System.out.println(header);
        for(AgeGroup ageGroup : AgeGroup.values()){
            double i = this.getInfectivity(ageGroup);
            double t = this.getToxigenicity(ageGroup);
            double r = this.getResistance(ageGroup);
            String row = ageGroup + "\t" + i + "\t" + t + "\t" + r;
            System.out.println(row);
        }
    }
    
}