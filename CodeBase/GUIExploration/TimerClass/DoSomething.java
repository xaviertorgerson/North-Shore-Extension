import java.util.TimerTask;
import java.util.Timer;
import java.io.BufferedReader;
import java.io.InputStreamReader;



class DoSomething extends TimerTask{
	
	static int counter;
	public static void main(String[] args)
	{
		counter = 0;
		
		System.out.println("Enter the simulation speed you want");
		int speed = getUserInt();
		Timer timer = new Timer();
		timer.schedule(new DoSomething(), 0, 1000/speed);
		

	}
	
	
	public void run()
	{
		counter++;
		System.out.println(counter);
	}
	
	
	
	public static int getUserInt()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int i = 0;
		try{i = Integer.parseInt(br.readLine());}
		catch(Exception e){}
		return i;
		
	}
	
	
	
}