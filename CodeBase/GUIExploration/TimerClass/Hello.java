import java.util.TimerTask;
import java.util.Timer;





class DoSomething extends TimerTask{
	
	static int counter;
	public static void main(String[] args)
	{
		counter = 0;
		
		
		Timer timer = new Timer();
		timer.schedule(new DoSomething(), 0, 5000);
		
		
		
		
		
		
		
	}
	
	
	public void run()
	{
		counter++;
		System.out.println(counter);
	}
	
	
	
	
	
	
	
}