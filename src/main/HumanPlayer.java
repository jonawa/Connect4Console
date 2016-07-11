package main;


import java.util.InputMismatchException;
import java.util.Scanner;

public class HumanPlayer implements IPlayer{
	private int playerID;

	
	/**
	 * Konstruktor für menschlichen Spieler, muss 1 oder 2 zuwegewiesen bekommen
	 * 
	 * @param playerID
	 */
	public HumanPlayer(int playerID) {
		// TODO Auto-generated constructor stub
		this.playerID=playerID;

		
	}
	/**
	 * Gibt den Zug des Menschlischen Spielers zurück, falls dieser einen falschen Zug gemacht hat wird allerdings -1 zurückgegeben
	 */
	@Override
	public int turn() {
		System.out.format("Spielzug eingeben 0-%d: ",Game.COLUMNS-1);
		Scanner sc = new Scanner(System.in);
		
		int column = -1;
		
			try{
				column = sc.nextInt();
				if (column <0 || column > Game.COLUMNS- 1)
					throw new InputMismatchException();
			}
			catch (InputMismatchException e){
				System.out.println("Fehler! Der eingebene Wert ist falsch");
			}

		
		
		return column;
	}

	@Override
	public void reactToWinOrLose(boolean win) {
		//leer lassen
		
	}

	@Override
	public int getPlayerID() {
		
		return playerID;
	}
	@Override
	public void setLearning(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
