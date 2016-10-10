import java.util.*;

public class PID{

	/*working variables*/
	long lastTime;
	double input, output, setPoint;
	double errSum, lastErr;
	double final kp = 1, ki = 1, kd = 1;
	Date d = new Date();

	public void compute()
	{
   		/*How long since we last calculated*/
   		double now = millis();
   		double timeChange = (double)(now - lastTime);
 	 
  	 	/*Compute all the working error variables*/
		double error = setPoint - input;
   		errSum += (error * timeChange);
   		double dErr = (error - lastErr) / timeChange;
  
   		/*Compute PID output*/
   		output = kp * error + ki * errSum + kd * dErr;
  
   		/*Remember some variables for next time*/
   		lastErr = error;
   		lastTime = now;
	}
}
