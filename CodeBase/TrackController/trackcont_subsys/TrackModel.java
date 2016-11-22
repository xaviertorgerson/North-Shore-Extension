package trackcont_subsys;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class TrackModel {

	static public ArrayList<Block> blockList;

	/*public static void main(String[] args) {

		blockList = new ArrayList<Block>();

		try (BufferedReader br = new BufferedReader(new FileReader("trackData.csv"))){
			String line;
			while ((line = br.readLine()) != null) {
				Block newBlock = new Block(line);
				blockList.add(newBlock);
			}
		}
		catch(Exception e) {
			System.out.println(e.getClass());
		}
	
		Scanner user_input = new Scanner(System.in);
		int inspectBlock;
		
		do {
			for (int k = 0; k < 50; k++) {
				System.out.println("\n");
			}
			System.out.print("What block would you like to inspect? ");
			inspectBlock = user_input.nextInt();
			getBlockWithNumber(inspectBlock).inspect();		
		} while (inspectBlock != 0);
		
	}*/
	
	public static Block getBlockWithNumber(int num) {
		for (int i = 0; i < blockList.size(); i++) {
			Block tempBlock = blockList.get(i);
			if(tempBlock.getNumber() == num) {
				return tempBlock;
			}
		}
		return null;
	}
	
}
