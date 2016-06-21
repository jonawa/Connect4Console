package main;

import util.Helper;

public class Game {
	private static boolean FINISHED;
	public static final int WINCOUNT = 4;
	public static final int COLUMNS = 7;
	public static final int ROWS = 6;
	/** Beispiel für ein 6*7 Board
	 * 							{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,1,0,1,1,0,0},
								{0,2,2,2,1,2,1}
		Beim Zugriff auf das board wird zuerst das die Zeile angegeben und dann die Spalte.
		Dabei ist oben links der erste Stein 0,0.
		Bsp hier Spieler 2 will seinen Stein in die erste Spalte werfen:
		board[5][0] = 2
		Sorgt dafür, dass in die 6.te Reihe in die 1.Spalte ein Stein vom Spieler 2 geworfen wird. 
								
								
	 */
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
		int row = -1;
		int count = 0;
		
		while(!FINISHED){
			
			if (count % 2 == 0){
				
				column = Spieler1.turn();
				row = placeDisk(column, Spieler1);
				
			}
				
			else{ 
				column = Spieler2.turn();
				row = placeDisk(column, Spieler2);
				
			}
			count++;
			
			//TODO Fehlerbehandlung
			if(row == -1){
				System.out.println("Fehler durch die Methode placeDisk, wahrscheinlich ist die Reihe voll deswegen -1");
			}
			
			
			System.out.println(Helper.convertIntBoardToString(board));
			
			
			if (checkWin(Spieler1,row,column)){
				Spieler1.reactToWinOrLose(true);
				Spieler2.reactToWinOrLose(false);
				System.out.println("---------------------------------");
				System.out.println("Spieler 1 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
			}
			if (checkWin(Spieler2,row, column)){
				Spieler1.reactToWinOrLose(false);
				Spieler2.reactToWinOrLose(true);
				System.out.println("---------------------------------");
				System.out.println("Spieler 2 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
			}
			
		}
		
	}
	/**
	 * Resets oder wenn noch nicht vorhanden, initialisiert das Board
	 */
	private static void resetBoard() {
		
		if (board == null){
			board = new int[ROWS][COLUMNS];
		}
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLUMNS; j++){
				board[i][j]=0;
			}
		}
	}
		
	

	public static void main(String[] args) {
		//testCheck4Win();
		playGame();
		
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
	
		if(checkWinRow( player,  row,  column))
			return true;
		if(checkWinColumn( player,  row,  column))
			return true;
		if(checkWinDiagonal1( player,  row,  column))
			return true;
		if(checkWinDiagonal2( player,  row,  column))
			return true;
		
		return false;
	}
	
	private static boolean checkWinDiagonal2(IPlayer player, int row, int column) {
		//rechts nach links
			int tokensfound=0;
			int p = player.getPlayerID();
				
			int j= row+1;
			//Schaue rechts:
			for (int i=column; i<=COLUMNS-1; i++){
				j--;
				if (j>=0 && board[j][i]==p){
					tokensfound++;
				}
					else {break;}
			}
			j= row;
			//Schaue links:
			for (int i=column-1; i>=0; i--){
				j++;
				if (j<=ROWS-1 && board[j][i]==p){
					tokensfound++;
				}
				else {break;}
			}
			return false;
	}
	
	private static boolean checkWinDiagonal1(IPlayer player, int row, int column) {
		// links nach rechts
		int tokensfound=0;
		int p = player.getPlayerID();
		
		int j= row-1;
		//Schaue rechts:
		for (int i=column; i<=COLUMNS-1; i++){
			j++;
			if (j<=ROWS-1 && board[j][i]==p){
				tokensfound++;
				System.out.println("Diagonale1++");
			}
			else {break;}
		}
		j= row;
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			j--;
			if (j>=0 && board[j][i]==p){
				tokensfound++;
			}
			else {break;}
		}
		return false;
	}

	private static boolean checkWinColumn(IPlayer player, int row, int column) {
		int tokensfound=0;
		int p = player.getPlayerID();
				
		
		// Schaue in der Spalte nach unten:
		for (int i=row; i<=ROWS-1; i++){
			if (board[i][column]==p){
				tokensfound++;
			}
			else {
				break;
			}
		}
		System.out.println("Column: Tokensfound "+ tokensfound);
		if (tokensfound>=WINCOUNT){
			return true;
		}
		return false;
		
	}

	private static boolean checkWinRow(IPlayer player, int row, int column) {
		
		int tokensfound=0;
		int p = player.getPlayerID();
		
		
		//Schaue rechts:
		for (int i=column; i<=COLUMNS-1; i++){
			if (board[row][i]==p){
				tokensfound++;
			}
			else {
				break;
			}
		}
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			if (board[row][i]==p){
				tokensfound++;
			}
			else {
				break;
			}
		}
		System.out.println("Row: Tokensfound "+ tokensfound);
		if (tokensfound>=WINCOUNT){
			return true;
		}
		
		return false;
	}

	/**
	 * Board wird mit dieser Methode verändert.
	 * int Rückgabe um zu überprüfen ob alles geklappt hat
	 * @param column
	 * @param Player, der den Stein wirft
	 * @return Rückgabe der Zeile, in die geworfen wird
	 * 			-1 wenn die Spalte voll oder sonstiger Fehler
	 */
	public static int placeDisk(int column, IPlayer player){
		//Beginne in der untersten Reihe
		int row = ROWS -1;
		
		//Bis zur obersten Reihe:
		for(int i = row; i>=0;i-- ){
			//Sobald ein Feld leer ist, gibt die Zeile dieses Felds zurück
			if(board[i][column] == 0){
				
				board[i][column] = player.getPlayerID(); 
				return i;
			}
				
		}
			
		return -1;
	}
	
	public static void printBoard(){
		
	}
	
	private static void testCheck4Win() {
		
		//Funktioniert nur mit WINCOUNT = 4 und 7*6 Spielfeld, also vor dem Testen die Statics so anpassen
		/**
		int[][] testboard  = {  {0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0}
		};
		**/
		IPlayer player1 = new HumanPlayer(1);
		IPlayer player2 = new HumanPlayer(2);
		
		int[][] testboard  = {  {1,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,1,0,1,1,0,0},
								{0,2,2,2,1,2,1}
		};
		
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(testboard));
		System.out.println("--------------------------------------");
		System.out.println("Spieler 2 platziert seinen Stein in Reihe 0");
		testboard[5][0] = 2;
		Game.board = testboard;
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(Game.board));
		System.out.println("--------------------------------------");
		
		System.out.println("Spieler 2 sollte gewonnen haben, weil er als letztes in Reihe 0 seinen Stein platziert hat: "
							+ checkWinRow(player2, 5, 0) );
		
		
	
		//ROW Testen
		int[][] testboard2  = {  {0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{2,0,0,0,0,0,0},
								{2,1,0,1,1,0,0},
								{1,2,2,2,1,2,1}
		};
		
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(testboard2));
		System.out.println("--------------------------------------");
		System.out.println("Spieler 2 platziert seinen Stein in Reihe 0");
		testboard2[2][0] = 2;
		Game.board = testboard2;
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(Game.board));
		System.out.println("--------------------------------------");
		
		System.out.println("Spieler 2 sollte gewonnen haben, weil er als letztes in Reihe 0 seinen Stein platziert hat: "
							+ checkWinColumn(player2, 2, 0) );		
		
		
		
		
		
		
		
		
	}

}
