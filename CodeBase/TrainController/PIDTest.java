import java.util.*;

public class PIDTest{

	public static Scanner keyboard = new Scanner(System.in);
	public static PID pid = new PID(1000);

	public static void main(String[] args)
	{
		double curSpeed = 0;
		double speedReq = 0;
		double powerReq = 0;

		while(true){
			System.out.println("Enter curSpeed: ");
			curSpeed = keyboard.nextDouble();
			System.out.println("Enter speedReq: ");
			speedReq = keyboard.nextDouble();

			powerReq = pid.update(curSpeed, speedReq);

			System.out.println("The PID generated power request is: " + powerReq);
		}
	}
}
