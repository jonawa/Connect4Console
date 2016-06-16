package main;

import java.io.IOException;
import java.io.InputStream;

public class HumanPlayer implements IPlayer{
	private int playerID;
	
	public HumanPlayer(int playerID) {
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public int turn() {
		System.out.println("Spielzu eingeben 0-6:");
		InputStream in = System.in;
		int column = -1;
		try {
			column = in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("column");
		return column;
	}

	@Override
	public void reactToWinOrLose(boolean win) {
		//leer lassen
		
	}

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}

}
