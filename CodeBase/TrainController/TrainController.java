/**
 *
 * @author gdgrube
 */

public class TrainController {
    
	//input variables
	public final int trainID;
    float curSpd = 0;
    float spdReq = 0;
    int setPoint = 0;
   
    //output variables
    int powReq = 0;
    int lightReq = 0;
    int acReq = 0;
    int heatReq = 0;
    int lDoorReq = 0;
    int rDoorReq = 0;
    boolean autoReq = false;
    boolean adReq = false;
    boolean eStopReq = false;
	
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
