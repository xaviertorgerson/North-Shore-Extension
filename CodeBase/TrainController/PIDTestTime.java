import java.util.*;

public class PIDTestTime{

	public static Scanner keyboard = new Scanner(System.in);
	public static PID pid = new PID(1000);

	public static void main(String[] args) throws InterruptedException
	{
		double curSpeed = 0;
		double speedReq = 0;
		double powerReq = 0;
		double m = 10000;
	
		System.out.println("Enter speedReq: ");
		speedReq = keyboard.nextDouble();
		
		System.out.println("Enter intitial speed: ");
		curSpeed = keyboard.nextDouble();
		

		
		while(true){
		
			powerReq = pid.update(curSpeed, speedReq);
			System.out.println("\nPID generated power request: " + powerReq);
			
			curSpeed = (powerReq)/(m) + curSpeed;
			
			System.out.println("speedReq: " + speedReq + "\ncurSpeed: " + curSpeed );
		
			Thread.sleep(1000);
		}
	}
}
