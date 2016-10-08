/**
 *
 * @author gdgrube
 */

public class TrainController {
    
	//input variables
	public final int trainID;
    float curSpd;
    float spdReq;
    int setPoint;
   
    //output variables
    int powReq;
    int lightReq;
    int acReq;
    int heatReq;
    int lDoorReq;
    int rDoorReq;
    boolean autoReq;
    boolean adReq;
    boolean eStopReq;
	
	//CONSTRUCTOR
	public TrainController(int ID){
		trainID = ID;
	}
    
	//MUTATORS
    public int setLights(int req){
        lightReq = req;
        return lightReq;  
    }
    
    public int setAC(int req){
        acReq = req;
        return req;
    }
    
    public int setHeat(int req){
        heatReq = req;
        return heatReq;
    }
    
    public int setLDoor(int req){
        lDoorReq = req;
        return lDoorReq;
    }
	
	public int setRDoor(int req){
        rDoorReq = req;
        return rDoorReq;
    }
    
    public boolean setAuto(boolean req){
        autoReq = req;
        return autoReq;
    }
    
    public boolean setAd(boolean req){
        adReq = req;
        return adReq;
    }
    
    public float setSpdReq(float req){
        spdReq = req;
        return spdReq;
    }
	
	public void constructVP(){
		
	}
	
	public boolean setEStop(boolean req){
		eStopReq = req;
		return eStopReq;
	}
}
