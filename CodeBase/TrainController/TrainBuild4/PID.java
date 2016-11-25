public class PID{

  	final float kp = 500, ki = 0, kd = 0;                        //instance data
	float errSum = 0, lastErr = 0;
	float freq;

	public PID(){                                                 //constructor
	}

	public float update(float curSpd, float setPoint, float timeChange){
		float error = setPoint - curSpd;                          //Compute all the working error variables
   		errSum += (error * timeChange);
   		float dErr = (error - lastErr) / timeChange;
  
   		float power = kp * error + ki * errSum + kd * dErr;       //Compute PID output with weight constants
 	 
   		lastErr = error;                                           //Remember some variables for next time

   		return power;                                              //return power 
	}
}