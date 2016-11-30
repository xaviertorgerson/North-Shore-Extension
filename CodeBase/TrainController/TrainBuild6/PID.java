public class PID{

  	final float kp = 50, kd = 2, ki = (float) 4.5;                         //instance data
	float errSum = 0, lastErr = 0;
	float freq;

	public PID(){                                                 //constructor
	}

	public float update(float curSpd, float setPoint, float timeChange){
		timeChange = timeChange;
		float error = setPoint - curSpd;                          //Compute all the working error variables
   		errSum += (error * timeChange);
   		float dErr = (error - lastErr) / timeChange;
  
   		float power = kp * error + ki * errSum + kd * dErr;       //Compute PID output with weight constants
 	 
   		lastErr = error;                                           //Remember some variables for next time

		if(power < 0)
			power = 0;
   		return power;                                              //return power 
	}
}
