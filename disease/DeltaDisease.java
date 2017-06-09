
class DeltaDisease extends DiseaseBlueprint {
    
    @Override
    public String getName() {
        return "Delta Disease";
    }
    
    @Override
    public double getInfectivity(AgeGroup ageGroup) {
        switch(ageGroup){
            case CHILD: return 0.1;
            case TEEN: return 0.5;
            case ADULT: return 0.3;
            case ELDER: return 0.1;
            default: return 0.0;
        }
    }
    
    @Override
    public double getToxigenicity(AgeGroup ageGroup) {
        return 0.25;
    }
    
    @Override
    public double getResistance(AgeGroup ageGroup) {
        switch(ageGroup){
            case CHILD: return 0.1;
            case TEEN: return 0.1;
            case ADULT: return 0.1;
            case ELDER: return 0.7;
            default: return 0.0;
        }
    }
    
    private int exits = 0;
    
    @Override
    public DiseaseAction move(SimulatedHost host, int energy) {
        DiseaseAction action = DiseaseAction.MULTIPLY;
        if(host.isIncubated()){
            action = DiseaseAction.RELEASE;
        }
        else if(host.isLatent()){
            if(exits < 3){
                action = DiseaseAction.EXIT;
                exits++;    
            }
            else{
                action = DiseaseAction.MULTIPLY;
            }
        }
        else{
            action = DiseaseAction.MULTIPLY;
        }
        return action;
    }
    
}