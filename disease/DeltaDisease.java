
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
    
    private boolean alternator = false;
    
    @Override
    public DiseaseAction move(SimulatedHost host) {
        DiseaseAction action = DiseaseAction.MULTIPLY;
        if(host.isIncubated()){
            action = DiseaseAction.RELEASE;
        }
        else if(host.isLatent()){
            if(alternator){
                action = DiseaseAction.MULTIPLY;
            }
            else{
                action = DiseaseAction.EXIT;
            }
            alternator = !alternator;
        }
        else{
            action = DiseaseAction.MULTIPLY;
        }
        return action;
    }
    
}