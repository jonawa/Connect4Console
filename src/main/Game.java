package main;

import util.Helper;

public class Game {
	private static boolean FINISHED;
	public static final int WINCOUNT = 4;
	public static final int COLUMNS = 7;
	public static final int ROWS = 6;
	public static int[][] board;

	
	public Game() {
		// TODO Auto-generated constructor stub
	}
	
	public static void playGame(){
		
		//Erzeuge leeres Board.
		
		resetBoard();

		
		
		
		IPlayer Spieler1 = new HumanPlayer(1);
		IPlayer Spieler2 = new HumanPlayer(2);
		FINISHED=false;
		
		int column = -1;
		int count = 0;
		
		while(!FINISHED){
			
			if (count % 2 == 0){
				column = Spieler1.turn();
				
			}
				
			else{ 
				column = Spieler2.turn();
			}
			count++;
			
			
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
	/**
	 * Resets oder wenn noch nicht vorhanden, initialisiert das Board
	 */
	private static void resetBoard() {
		
		if (board == null){
			board = new int[COLUMNS-1][ROWS-1];
		}
		for (int i=0; i<COLUMNS; i++) {
			for (int j=0; j<ROWS; j++){
				board[i][j]=0;
			}
		}
	}
		
	

	public static void main(String[] args) {
		testCheck4Win();
		
	}
	


	/**
	 * Prüft nachdem ein Stein in eine Spalte geworfen worden ist, ob der Spiele der mitgegeben wird gewonnen hat.
	 * Ruft 4 Methoden auf, die jeweils die unterschiedlichen Fälle für ein N gewinnt abdecken
	 * N ist dabei in der Game Klasse als statische Variable definiert.
	 * 
	 * @param player Spieler für den geprüft werden soll, ob dieser gewonnen hat.
	 * @param row
	 * @param column
	 * @return
	 */
	
	
	public static boolean checkWin(IPlayer player, int row, int column){
		
		//Methoden sollen das für den statischen WINCOUNT prüfen, nicht für 4
	
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
	
	private static void testCheck4Win() {
		
		//Funktioniert nur mit WINCOUNT = 4 und 7*6 Spielfeld, also vor dem Testen die Statics so anpassen
		/**
		int[][] board  = {  {0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0}
		};
		**/
		IPlayer player1 = new HumanPlayer(1);
		IPlayer player2 = new HumanPlayer(2);
		
		int[][] board  = {  {1,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,1,0,1,1,0,0},
							{0,2,2,2,1,2,1}
		};
		Game.board = board;
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(Game.board));
		System.out.println("--------------------------------------");
		System.out.println("Spieler 2 platziert seinen Stein in Reihe 0");
		board[5][0] = 2;
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(Game.board));
		System.out.println("--------------------------------------");
		
		System.out.println("Spieler 2 sollte gewonnen haben, weil er als letztes in Reihe 0 seinen Stein platziert hat: "
							+ checkWinRows(player2, 5, 0) );
		
		
		
		
		
		
		
	}

}
