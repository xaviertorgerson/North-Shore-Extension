public class PID{

  final double kp = 500, ki = 0, kd = 0;                        //instance data
	double errSum = 0, lastErr = 0;
	double freq;

	public PID(){                                                 //constructor
	}

	public double update(double curSpd, double setPoint, double timeChange){
		  double error = setPoint - curSpd;                          //Compute all the working error variables
   		errSum += (error * timeChange);
   		double dErr = (error - lastErr) / timeChange;
  
   		double power = kp * error + ki * errSum + kd * dErr;       //Compute PID output with weight constants
 	 
   		lastErr = error;                                           //Remember some variables for next time

   		return power;                                              //return power 
	}
}
