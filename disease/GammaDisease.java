
class GammaDisease extends DiseaseBlueprint {
    
    @Override
    public String getName() {
        return "Gamma Disease";
    }
    
    @Override
    public double getInfectivity(AgeGroup ageGroup) {
        return 0.1;
    }
    
    @Override
    public double getToxigenicity(AgeGroup ageGroup) {
        return 0.25;
    }
    
    @Override
    public double getResistance(AgeGroup ageGroup) {
        return 0.1;
    }
    
    private int lastEnergy = 0;
    
    @Override
    public DiseaseAction move(SimulatedHost host) {
        DiseaseAction action = DiseaseAction.MULTIPLY;
        if(host.isIncubated()){
            int energyLoss = lastEnergy - host.getEnergy();
            if(energyLoss > host.getEnergy()){
                action = DiseaseAction.MULTIPLY;
            }
            else{
                double rand = Math.random();
                if(rand < 0.5){
                    action = DiseaseAction.RELEASE;
                }
                else{
                    action = DiseaseAction.EXIT;
                }
            }
        }
        else{
            action = DiseaseAction.MULTIPLY;
        }
        lastEnergy = host.getEnergy();
        return action;
    }
    
}