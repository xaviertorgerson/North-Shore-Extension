public class PID{

	//instance data
	final double kp = 1, ki = 1, kd = 1;
	double errSum = 0, lastErr = 0;
	double freq;

	//constructor
	public PID(double f){
		
		freq = f;
		/*
		Timer timer = new Timer();
		timer.schedule(new runUpdate(), 0, freq);
		*/
	}


	//mutators
	public double update(double input, double setPoint){
		System.out.println("Update Called");
	  	double timeChange = freq; //milliseconds
 	
  	 	//Compute all the working error variables
		double error = setPoint - input;
   		errSum += (error * timeChange);
   		double dErr = (error - lastErr) / timeChange;
  
   		//Compute PID output
   		double output = kp * error + ki * errSum + kd * dErr;
 	 
   		//Remember some variables for next time
   		lastErr = error;

   		return output;
	}

	/*
	class runUpdate extends TimerTask {
    	public void run() {
    		update();
    	}
    }*/	
}
