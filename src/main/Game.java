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
			
			//Spieler.turn() oder Spieler2.turn() gibt int zur�ck
			
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
	
	/**
	 * Pr�ft nachdem ein Stein in eine Spalte geworfen worden ist, ob der Spiele der mitgegeben wird gewonnen hat.
	 * Ruft 4 Methoden auf, die jeweils die unterschiedlichen F�lle f�r ein N gewinnt abdecken
	 * N ist dabei in der Game Klasse als statische Variable definiert.
	 * 
	 * @param player Spieler f�r den gepr�ft werden soll, ob dieser gewonnen hat.
	 * @param row
	 * @param column
	 * @return
	 */
	
	
	public static boolean checkWin(IPlayer player, int row, int column){
		
		//Methoden sollen das f�r den statischen WINCOUNT pr�fen, nicht f�r 4
	
		if(checkWinRows( player,  row,  column))
			return true;
		if(checkWinColumns( player,  row,  column))
			return true;
		if(checkWinDiagonal1( player,  row,  column))
			return true;
		if(checkWinDiagonal2( player,  row,  column))
			return true;
		
		return false;
	}
	
	private static boolean checkWinDiagonal2(IPlayer player, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static boolean checkWinDiagonal1(IPlayer player, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean checkWinColumns(IPlayer player, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean checkWinRows(IPlayer player, int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Board wird mit dieser Methode ver�ndert.
	 * boolean R�ckgabe um zu �berpr�fen ob alles geklappt hat
	 * @param row
	 * @return R�ckgabe der Zeile, in die geworfen wird
	 * 			-1 wenn die Spalte voll oder sonstiger Fehler
	 */
	public static int placeDisk(int row){
		return -1;
	}
	
	public static void printBoard(){
		
	}

}
