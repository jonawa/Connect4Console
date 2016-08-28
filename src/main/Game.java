package main;


import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

import ai.NNPlayer;
import ai.NNPlayer2;
import ai.QPlayer;
import db.Array2DWrapper;
import db.TestDB;
import db.TestDB2;
import db.TurnWrapper;
import ai.QPlayer2;
import ai.RandomPlayer;
import util.Helper;

public class Game {
	public static boolean BOARDISEMPTY;
	private static boolean FINISHED;
	
	//__________________________________________________________________________
	// Spieleinstellungen: 
	public static final int WINCOUNT = 3;
	
	public static final int COLUMNS = 5;
	public static final int ROWS = 4;
	
	
	//__________________________________________________________________________

	
	public static int tokensOnField;
	/** Beispiel für ein 6*7 Board . [6][7]
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
	private static int[][] board;
	//TODO immer mit getter jetzt auf board zugreifen. Die einzige Methode, die board verändern darf sollte placeDisk sein.

	/**
	 * Board sollte eigentlich wirklich nicht public sein und darf nicht von anderen Klassen verändert werden.
	 * Daher dieser getter!
	 * 
	 * @return
	 */
	public static int[][] getBoard() {
		return board;
	}
	

	public Game() {
		// TODO Auto-generated constructor stub
	}
	
	public static void playGame(IPlayer Spieler1, IPlayer Spieler2){
		
		//Erzeuge leeres Board.
		resetBoard();
		
		System.out.println(Helper.convertIntBoardToString(board));
	

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
			

			//TODO WICHTIG : Abprüfen ob ein Untentschieden vorliegt, da alle Felder voll sind.
			if (checkWin(1, row,column)){

				Spieler1.reactToWinOrLose(true);
				Spieler2.reactToWinOrLose(false);
				System.out.println("---------------------------------");
				System.out.println("Spieler 1 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
			}
			if (checkWin(2, row, column)){
				Spieler1.reactToWinOrLose(false);
				Spieler2.reactToWinOrLose(true);
				System.out.println("---------------------------------");
				System.out.println("Spieler 2 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
			}
			if (boardIsFull() && !FINISHED){
				System.out.println("---------------------------------");
				System.out.println("Spielergebnis: Unentschieden");
				System.out.println("---------------------------------");
				FINISHED = true;
			}
			
		}
		
	}
	/**
	 * Resets oder wenn noch nicht vorhanden, initialisiert das Board
	 */
	
	

	/**
	 * Spieler ist der QPlayer, Spieler 2, die KI, mit der trainiert werden soll
	 * @param Spieler1
	 * @param Spieler2
	 * @param numberTrainGames
	 * @param abwechselnd Wenn True lernt der QPlayer beide Seiten, wenn false lernt er nur die von einer Seite
	 */
	public static void trainQPlayer(IPlayer Spieler1, IPlayer Spieler2, int numberTrainGames, boolean abwechselnd){
		
		//Erzeuge leeres Board.
		resetBoard();
	

		FINISHED=false;
		
		int column = -1;
		int row = -1;
		int count = 0;
		int playcount = 0;

		int indicator;
		if (abwechselnd) indicator=1;
		else indicator=0;
		
		Spieler1.setLearning(true);
		Spieler2.setLearning(true);

		while(playcount <= numberTrainGames){

			
			
			if ((count+playcount*indicator) % 2 == 0){
				
				column = Spieler1.turn();
				row = placeDisk(column, Spieler1);
				
			}
				
			else{ 
				column = Spieler2.turn();
				row = placeDisk(column, Spieler2);
				
			}
			count++;
			tokensOnField++;
			
			//TODO Fehlerbehandlung
			if(row == -1){
				System.out.println("Fehler durch die Methode placeDisk, wahrscheinlich ist die Reihe voll deswegen -1");
			}
			
			
			System.out.println(Helper.convertIntBoardToString(board));
			

			//TODO WICHTIG : Abprüfen ob ein Untentschieden vorliegt, da alle Felder voll sind.
			if (checkWin(1, row,column)){

				Spieler1.reactToWinOrLose(true);
				Spieler2.reactToWinOrLose(false);
				System.out.println("---------------------------------");
				System.out.println("Spieler 1 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
				resetBoard();
				count = 0;
				playcount++;
			}
			if (checkWin(2, row, column)){
				Spieler1.reactToWinOrLose(false);
				Spieler2.reactToWinOrLose(true);
				System.out.println("---------------------------------");
				System.out.println("Spieler 2 hat gewonnen");
				System.out.println("---------------------------------");
				FINISHED = true;
				resetBoard();
				count = 0;
				playcount++;
			}
			if (boardIsFull()){
				System.out.println("---------------------------------");
				System.out.println("Spielergebnis: Unentschieden");
				System.out.println("---------------------------------");
				FINISHED = true;
				resetBoard();
				count = 0;
				playcount++;
			}
			
		}
		
		java.awt.Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null,
			    "Training  des QPlayer beendet!");

		

		
	}
	
	/**
	 * 
	 * @param numberOfGames
	 * @param Spieler1
	 * @param Spieler2
	 * @param abwechselnd Wenn true, Spielen die beiden Spieler im Turnier abwechselnds
	 * 						Wenn false, spielt immer SPieler 1 zuerst
	 * @return double[0] => Anzahl der gewonnen Spiele von Spieler 1
	 * 			double[1] => Anzahl der gewonnen Spiele von Spieler 2
	 * 			double[2] ==> Anzahl der Spiele, die unentschieden ausgegangen sind.
	 * 
	 */

	public static int[] playTournament(int numberOfGames, IPlayer Spieler1, IPlayer Spieler2, boolean abwechselnd){

		
	//	QPlayer2 qp = (QPlayer2)Spieler1; //Typecast um getAnzUnbekannterZustaenste() aufzurufen
//		qp.setAnzUnbekannteZustaende(0);
//		qp.setAnzZuegeMitWertungNulll(0);
//		
		

		int numberOfWinsPlayer1 = 0;
		int numberOfWinsPlayer2 = 0;
		int numberOfDraws = 0;
		
		int[] result = new int[3];
		
		Spieler1.setLearning(false);
		Spieler2.setLearning(false);
		
		
		int column = -1;
		int row = -1;
		
		int winningsOfPlayer1 = 0;
		int winningsOfPlayer2 = 0;
		
		int indicator;
		if (abwechselnd) 
			indicator=1;
		else indicator=0;
		
		for (int i = 1; i <= numberOfGames; i++) {
			int numberOfMoves = 0;
			int count = 0;
			resetBoard();
			FINISHED = false;
			
			while(!FINISHED){
			
				if (((count+i*indicator) % 2) == 0){
					column = Spieler1.turn();
					row = placeDisk(column, Spieler1);
					numberOfMoves++;
				}
				else{ 
					column = Spieler2.turn();
					row = placeDisk(column, Spieler2);
					numberOfMoves++;
				}
				count++;
				tokensOnField++;
			
				//TODO Fehlerbehandlung
				if(row == -1){
					System.out.println("Fehler durch die Methode placeDisk, wahrscheinlich ist die Reihe voll deswegen -1");
				}
			
			
				System.out.println(Helper.convertIntBoardToString(board));
			

				//TODO WICHTIG : Abprüfen ob ein Untentschieden vorliegt, da alle Felder voll sind.
				if (checkWin(1, row,column)){
					winningsOfPlayer1++;
					System.out.println("---------------------------------");
					System.out.println("Spieler 1 hat nach "+ numberOfMoves + " Spielzuegen gewonnen");
					System.out.println("---------------------------------");
					FINISHED = true;
					count = 0;
					
					numberOfWinsPlayer1++;
				}
				
				if (checkWin(2, row, column)){
					winningsOfPlayer2++;
					System.out.println("---------------------------------");
					System.out.println("Spieler 2 hat nach "+ numberOfMoves + " Spielzuegen gewonnen");
					System.out.println("---------------------------------");
					FINISHED = true;
					count = 0;
					
					numberOfWinsPlayer2++;
				}
				if (boardIsFull()){
					System.out.println("---------------------------------");
					System.out.println("Spielergebnis: Unentschieden");
					System.out.println("---------------------------------");
					FINISHED = true;
					count = 0;
					
					numberOfDraws++;
				}
				
				
			}	
			

		}
		System.out.println("Anzahl der gespielten Spiel: " + numberOfGames);
		if (winningsOfPlayer1 == winningsOfPlayer2) {
			System.out.println("Das Turnier ist unentschieden!");
		}
		else {
			if (winningsOfPlayer1 > winningsOfPlayer2) {
				System.out.println("Sieger des Turniers ist Spieler 1!");
			}
			else {
				System.out.println("Sieger des Turniers ist Spieler 2!");
			}
		}
		
		//Schreiben die Daten in das Array
		result[0] = numberOfWinsPlayer1;
		result[1] = numberOfWinsPlayer2;
		result[2] = numberOfDraws;
		
		System.out.println("Anzahl der gewonnenen Spiele von Spieler 1: " + winningsOfPlayer1);
		System.out.println("Anzahl der gewonnenen Spiele von Spieler 2: " + winningsOfPlayer2);
		int sum = winningsOfPlayer1 + winningsOfPlayer2;
		int draws = numberOfGames - sum;
		System.out.println("Anzahl der unentschiedenen Spiele: " + draws);
		//System.out.println("Anzahl unbekannter Spielzustände: "+ qp.getAnzUnbekannteZustaende());
		//System.out.println("Anzahl der gewählten Optionen mit Wertung 0: "+ qp.getAnzZuegeMitWertungNulll());
		
		return result;
	}
	
	/**
	 * Methode, die zwei Spieler gegeneinander spielen lässt.
	 * ACHTUNG: Es werden nur die Züge von Spieler 2 gespeichert.
	 * Dabei am Ende eine ArrayList zurückgegeben, die TurnWrapper beinhaltet.
	 * TurnWrapper geben je einen Spielstand (state) und den Zug vom Spieler auf diesem Spielfeld (action) zurück.
	 * 
	 * Intern benutzt die Methode zunächst eine HashMap, um neue Spielzüge effizienter zu speichern.
	 * Anschließend wird die HashMap in eine ArrayList umgewandelt, die zurückgegeben wird.
	 * @param Spieler1 
	 * @param Spieler2
	 * @param numGames
	 * @return ArrayList mit TurnWrappern
	 */
	public static ArrayList<TurnWrapper> generateDataSetForNN(IPlayer Spieler1, IPlayer Spieler2, int numGames){
		
		resetBoard();
		
		System.out.println("Starte Generation von Datenset für NN");
		
		int column = -1;
		int row = -1;
		int count = 0;
		
		//Hier wird zuerst eine HashMap benutzt, die dann später in ein Array umgewandelt wird.
		HashMap<Array2DWrapper, Integer> dataSet = new HashMap<Array2DWrapper, Integer>();
		
		
		int i = 0;
		while (i < numGames){
			
			System.out.println(Helper.convertIntBoardToString(Game.getBoard()));
			if (count % 2 == 0){
				
				column = Spieler1.turn();
				row = placeDisk(column, Spieler1);
				
			}
				
			else{ 
				column = Spieler2.turn();
				
				
				//Das Datenset wird nur für Spieler 2 erstellt
				//Falls DatenSet diesen State und Action noch nicht kennt, füge ein, ansonsten passiert nichts.
				Array2DWrapper stateWrap = new Array2DWrapper(Helper.deepCopy2DArray(Game.getBoard()));
				
				//Falls State und Action nicht schon vorhanden, füge neues Set hinzu
				//stateWrap = Spielfeld, column = Action, die diesem Spielfeld ausgeführt wird (also Reihe in die geworfen wird)
				if (!dataSet.containsKey(stateWrap)){
					dataSet.put(stateWrap, column);
				}
				
				//jetzt wird der Stein platziert
				row = placeDisk(column, Spieler2);
				
			}
			count++;
			
			if(row == -1){
				System.out.println("Fehler durch die Methode placeDisk, wahrscheinlich ist die Reihe voll deswegen -1");
			}
			


			//Falls gewonnen, verloren oder untentschieden, einfach Board reseten und weitermachen
			if (checkWin(1, row,column)){
				resetBoard();
				count = 0;
				i++;
			}
			if (checkWin(2, row, column)){
				resetBoard();
				count = 0;
				i++;
			}
			if (boardIsFull()){
				count = 0;
				resetBoard();
				i++;
			}
			
		}
		System.out.println("Datenset Anzahl an Elementen: " + dataSet.size());
		System.out.println("Erstelle jetzt die ArrayList: ");
		
		
		//Umwandlung der HashMap in ein Array:
		ArrayList<TurnWrapper> dataSetArray = new ArrayList<TurnWrapper>(dataSet.size());
		
		for(Array2DWrapper state: dataSet.keySet()){
			TurnWrapper turn = new TurnWrapper(state.getArr(), dataSet.get(state).intValue());
			dataSetArray.add(turn);
			
		}
		System.out.println("Erstellen der ArrayList abgeschlossen");
		
		return dataSetArray;
		
	}
	
	private static void resetBoard() {
		
		if (board == null){
			board = new int[ROWS][COLUMNS];
		}
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLUMNS; j++){
				board[i][j]=0;
			}
		}
		BOARDISEMPTY=true;
		tokensOnField=0;
	}
	
	private static boolean boardIsFull(){
		for (int i=0; i<COLUMNS; i++){
			if(board[0][i]==0){
				return false;
			}
		}
		return true;
	}
		
	

	public static void main(String[] args) {
	
		//trainAndTestQ();
		trainAndTestNN();
		//TestDB2.getDB().loadDB("testSaveDB.ser");
		//trainQPlayer(qPlayer, normalKI, 1000);
		
		//playTournament(1000, qPlayer, normalKI);
		
		//generateDataSets();

		//playGame(new HumanPlayer(1), new NNPlayer2(2, COLUMNS, ROWS, WINCOUNT));

		//playGame(new HumanPlayer(1), new NNPlayer2(2));
		
		//im NNPlayer Kontruktor uebergeben: int playerID, int columns, int rows, int wincount, 
		//int hiddenLayer, double maxError, double learningRate, double momentum
		//generateDataSets();
		//playGame(new NNPlayer2(1, COLUMNS, ROWS, WINCOUNT, 27, 0.05, 0.2, 0.7) , new HumanPlayer(2));
		//playTournament(100, new NormalKI2(1), new NNPlayer2(2, COLUMNS, ROWS, WINCOUNT, 120, 0.05, 0.2, 0.7));

	}
	


	private static void trainAndTestNN() {
		IPlayer player1 = new NormalKI2(1);
		IPlayer player2 = new NNPlayer2(2);

		//generateDataSets();
		playTournament(10000,player1,player2,true);
//		playTournament(1000, new NormalKI(1), new NormalKI(2), true);
		
		
	}


	private static void trainAndTestQ() {
				
		IPlayer qPlayer = new QPlayer2(1);
		IPlayer normalKI = new NormalKI2(2);
		TestDB2.getDB().loadDB("testSaveDB.ser");

		//trainQPlayer(qPlayer, normalKI, 300000, true);

		

		playTournament(10000, qPlayer, normalKI,true);

		//trainQPlayer(qPlayer, normalKI, 1000);

		

		
		// Gib Spieleinstellungen aus:
		System.out.println("Anzahl der Datenbank-Elemente: " + TestDB2.getDB().getSize());
		TestDB2.getDB().saveDB("testSaveDB.ser");
//		playTournament(10, qPlayer, new HumanPlayer(2));
	}


	private static void generateDataSets() {
		
		//Anzahl der Spiele die gespielt werden soll:
		final int numberOfTrainingGames = 200;
		
		//generiert das Array
		ArrayList<TurnWrapper> list =  generateDataSetForNN(new NormalKI(1), new NormalKI(2),numberOfTrainingGames);
		
		
		//hier nur Ausgabe an Konsole zum Testen:
//		for(TurnWrapper turn : list){
//			System.out.println(Helper.convertIntBoardToString2(turn.getState()) +"\t" + turn.getAction());
//		}
		
		//schreibt das Array in die Datenbank
		//Name der Txt-Datei erstellt anhand des aktuellen Spielfelds und der Gewinnbedingung
		Helper.saveTurnWrapperArrayToTxt2(list, "dataset_5x4_3G_p2.txt");
		
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
	
	
	public static boolean checkWin(int player, int row, int column){
		
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
	
	private static boolean checkWinDiagonal2(int player, int row, int column) {
		//rechts nach links
			int tokensfound=0;
				
			int j= row;
			//Schaue rechts:
			for (int i=column; i<=COLUMNS-1; i++){
				if (j>=0 && board[j][i]==player){
					tokensfound++;
					j--;
				}
					else {break;}
			}
			j= row+1;
			//Schaue links:
			for (int i=column-1; i>=0; i--){
				if (j<=ROWS-1 && board[j][i]==player){
					tokensfound++;
					j++;
				}
				else {break;}
			}
			if (tokensfound>=WINCOUNT){
				return true;
			}
			return false;
	}
	
	private static boolean checkWinDiagonal1(int player, int row, int column) {
		// links nach rechts
		int tokensfound=0;
		
		int j= row;
		//Schaue rechts:
		for (int i=column; i<=COLUMNS-1; i++){
			if (j<=ROWS-1 && board[j][i]==player){
				tokensfound++;
				j++;
			}
			else {break;}
		}
		j= row-1;
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			if (j>=0 && board[j][i]==player){
				tokensfound++;
				j--;
			}
			else {break;}
		}
		if (tokensfound>=WINCOUNT){
			return true;
		}
		return false;
	}

	private static boolean checkWinColumn(int player, int row, int column) {
		int tokensfound=0;
		
		
		// Schaue in der Spalte nach unten:
		for (int i=row; i<=ROWS-1; i++){
			if (board[i][column]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		if (tokensfound>=WINCOUNT){
			return true;
		}
		return false;
		
	}

	private static boolean checkWinRow(int player, int row, int column) {
		
		int tokensfound=0;
		
		
		//Schaue rechts:
		for (int i=column; i<=COLUMNS-1; i++){
			if (board[row][i]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			if (board[row][i]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		
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
	private static int placeDisk(int column, IPlayer player){
		//Beginne in der untersten Reihe
		int row = ROWS -1;
		
		//Bis zur obersten Reihe:
		for(int i = row; i>=0;i-- ){
			//Sobald ein Feld leer ist, gibt die Zeile dieses Felds zurück
			if(board[i][column] == 0){
				
				//Stein wird ins Spielfeld geworfen:
				board[i][column] = player.getPlayerID(); 
				if (BOARDISEMPTY==true)
					{BOARDISEMPTY=false;}
				
				return i;
			}
				
		}
			
		return -1;
	}
	/**
	 * Mit dieser Methode kann man Testen ob es möglich ist einen Stein in die angebene Spalte zu werfen.
	 * 
	 * @param column Spalte in die geworfen werden soll
	 * @return wenn möglich wird die Zeile zurückgegeben, in der der Stein landet, wenn nicht möglich -1
	 */
	public static int placeDiskPossible(int column){
		

		//Beginne in der untersten Reihe
		int row = ROWS -1;
		
		for(int i = row; i>=0;i-- ){
			//Sobald ein Feld leer ist, gibt die Zeile dieses Felds zurück
			if(board[i][column] == 0){
				
				return i;
			}
				
		}
		throw new RuntimeException("In die Spalte soll geworfen werde aber das geht nicht: " + column);
		//return -5;
		
	}
	
	public static void printBoard(){
		
	}
	
	private static void testCheck4Win() {
		
		//Funktioniert nur mit WINCOUNT = 4 und 7*6 Spielfeld, also vor dem Testen die Statics so anpassen
		//testColumns();
		
		//testRows();
		
		testDiagonal();
	
	}

	private static void testDiagonal() {
		IPlayer player1 = new HumanPlayer(1);
		IPlayer player2 = new HumanPlayer(2);
		
		//ROW Testen
		int[][] testboard2  = {  {0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{2,2,1,0,1,2,0},
								{2,1,2,1,1,0,0},
								{1,2,1,2,1,2,1}
		};
		System.out.println("--------------TESTE DIAGONALE 1---------------------- \n \n \n");
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
							+ checkWin(2, 2, 0) );	
		
		System.out.println("\n \n \n --------------TESTE DIAGONALE 2---------------------- \n \n \n");
		System.out.println("Spieler 1 setzt seinen Stein in Reihe 5");
		testboard2[2][5] = 1;
		Game.board = testboard2;
		System.out.println("So sieht das Spielfeld aus:");
		System.out.println("--------------------------------------");
		System.out.println(Helper.convertIntBoardToString(Game.board));
		System.out.println("--------------------------------------");
		
		System.out.println("Spieler 1 sollte gewonnen haben, weil er als letztes in Reihe 0 seinen Stein platziert hat: "
							+ checkWin(1, 3, 4) );
		
		
		
		
	}

	private static void testRows() {
		IPlayer player1 = new HumanPlayer(1);
		IPlayer player2 = new HumanPlayer(2);
		
		//ROW Testen
		int[][] testboard2  = {  {0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0},
								{2,0,0,0,0,0,0},
								{2,1,0,1,1,0,0},
								{2,2,2,2,1,2,1}
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
							+ checkWinColumn(2, 2, 0) );		
		
		
		
	}

	private static void testColumns() {
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
							+ checkWinRow(2, 5, 0) );
		
		
	}

}
