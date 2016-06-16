package main;

public class Game {
	private static boolean FINISHED;
	public static final int WINCOUNT = 4;
	public static final int COLUMNS = 0;
	public static final int ROWS = 0;
	public static int[][] board;

	
	public Game() {
		// TODO Auto-generated constructor stub
	}
	
	public static void playGame(){
		
		IPlayer Spieler1 = new HumanPlayer(1);
		IPlayer Spieler2 = new HumanPlayer(2);
		
		while(!FINISHED){
			
			//Spieler.turn() oder Spieler2.turn() gibt int zurück
			
			int column = 0;
			int row = placeDisk(column);
			
			
			if (checkWin(Spieler1,row,column)){
				Spieler1.reactToWinOrLose(true);
				Spieler2.reactToWinOrLose(false);	
			}
			if (checkWin(Spieler2,row, column)){
				Spieler1.reactToWinOrLose(false);
				Spieler2.reactToWinOrLose(true);
			}
			
		}
		
	}
	public static void main(String[] args) {
		
	}
	
	public static boolean checkWin(IPlayer player, int row, int column){
		
		/**checkColumns(,Player)
		checkRows(,Player)
		checkDiagonal1(,PLayer)
		checkDiagonal2(,Player)
		**/
		
		return false;
	}
	
	/**
	 * Board wird mit dieser Methode verändert.
	 * boolean Rückgabe um zu überprüfen ob alles geklappt hat
	 * @param row
	 * @return Rückgabe der Zeile, in die geworfen wird
	 * 			-1 wenn die Spalte voll oder sonstiger Fehler
	 */
	public static int placeDisk(int row){
		return -1;
	}
	
	public static void printBoard(){
		
	}

}
