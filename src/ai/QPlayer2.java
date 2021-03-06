package ai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import db.Q_DB;
import main.Game;
import main.IPlayer;
import util.Helper;

public class QPlayer2 implements IPlayer {
	
	public static final double REWARD = 100.0; //wenn gewonnen
	public static final double PUNISHMENT = -100.0; //wenn verloren
	
	private final int playerID;
	private Q_DB Q;
	private double gamma;
	private int epsilon = 10;
	private double alpha = 0.01;
	
	/**
	 * Variable bestimmt ob der QPlayer neue Datenbankeintr�ge macht oder nicht
	 * wird im Turniermodus ausgestellt, da dort nur der vorhanden Stand getestet wird.
	 */
	private boolean learning=true;
	
	// zur Auswertung:
	private int anzUnbekannteZustaende;
	private int anzZuegeMitWertungNulll;
	
	private int[][] lastState;
	private int lastAction;
	
	public QPlayer2(int playerID) {

		this.playerID = playerID;
		Q = Q_DB.getDB();
		gamma = 0.8;
	}
	
	@Override
	public int turn() {
		
		//Wie sieht das Spielfeld gerade aus:
		int[][] currentState = Helper.deepCopy2DArray(Game.getBoard());
		
		//aus allen M�glichenActions, die beste.. oder eine zuf�llig ausw�hlen
		int[] actions = generateActions(currentState, true);
		int action = chooseBestAction(currentState, actions);	
			
		//Finde neuen Wert f�r Q, au�er es ist mein letzter Zug:
		System.out.println(Game.tokensOnField);
		if (Game.tokensOnField<(Game.ROWS*Game.COLUMNS)-2){
			if(learning){
				//Datenbank mit neuem Wert updaten:	
				double newQValue = (1-alpha)*Q.getValueOfStateAndAction(currentState,  action)+ 
					alpha*(gamma*avgValueForNextStateAllActions(currentState, action));
				Q.update(currentState, action, newQValue);
			}
		}
		
				
		/*Speichere den aktuellen State und die ausgew�hlte Action. Falls die KI verliert, wird sie dazu aufgefordert auf das Verlieren zu reagieren
		 * (reactToWinrOrLose).
		 * Die KI muss also wissen, was sie gemacht hat um Q zu aktualisieren und sich zu bestrafen.
		 */
		lastState = currentState;	//tiefe Kopie erstellen, wurde oben bereits gemacht
		lastAction = action;
		
		
		return action;
	}
	
	/**
	 * Sobald der Algo an einem Punkt angelangt ist, an dem er die n�chsten m�glichen Z�ge noch nicht in seiner Datenbank hat (Am Anfang ist das immer der Fall)
	 * wird diese Methode ausgef�hrt.
	 * 
	 * Die Methode generiert alle m�glichen Z�ge (actions) basierend auf dem aktuellen Spielfeld (state) und f�gt f�r diesen State dann alle m�glichen 
	 * Actions mit Value 0 in die Datenbank ein.
	 * 
	 * @param state Spielfeld
	 * @param possibleActions 
	 */
	public void initializeState(int[][] state, int[] possibleActions){
		
		for(int action : possibleActions){
			Q.put(state, action, 0.0);
			
		}
	}

	@Override
	public void reactToWinOrLose(boolean win) {
		//teste ob der letzte State nicht der aktuelle State ist und wirklich eine tiefe Kopie erstellet worden ist:
		if(Arrays.deepEquals(Game.getBoard(),lastState)){
			throw new RuntimeException("Die Q KI hat sich nicht den letzten Spielstand gemerkt.");
		}	
		//wenn gewonnen, muss nichts passieren, KI hat sich schon selbst belohnt, als der Gewinnzug ausgef�hrt wurde( mit REWARD)
		if(win){
			Q.update(lastState, lastAction, REWARD); // 1.0 richtig?
		}
		else{
			Q.update(lastState, lastAction, PUNISHMENT);	
		}
		
	}

	/**
	 * Nachdem die beste Aktion vom QPlayer ausgew�hlt wurde, wird diese Methode aufgerufen.
	 * Es werden die n�chsten m�glichen States f�r den QPlayer betrachet, und von dieses States die
	 * die Action gesucht mit dem h�chsten Value. Dieser Value wird zur�ckgegeben.
	 * Da der QPlayer nicht direkt wieder dran ist, m�ssen auch alle unterschiedlichen M�glichkeiten f�r
	 * den Gegner durchgegangen werrden.
	 * 
	 * ACHTUNG: WIRD AKTUELL NICHT MEHR VERWENDET!!
	 * 
	 * @param state Spielfeld bevor der QPlayer seinen Stein geworfen hat.
	 * @param action Zug der vom QPlayer ausgew�hlt worden ist.
	 * @return erfolgsversprechenster Wert, den der QPlayer aus diese State mit dieser Action erreichen kann
	 */
	private double maxValueForNextStateAllActions(int[][] state, int action) {
		
		double maxOfallPossibleActions = Double.MIN_VALUE;
		
		//Simulieren, QPlayer wirft seinen Stein, richtiges Board wird erst ver�ndert nach dem Zug
		int[][] nextStateOpponent = Helper.deepCopy2DArray(state); 
		int row = Game.placeDiskPossible(action);
		nextStateOpponent[row][action] = playerID;
		
		//Simulieren wo Gegner hinwerfen k�nnte - Array mit den m�glcihen Spalten:
		int[] allActionOpponent = generateActions(nextStateOpponent, false);
		
		//F�r jede m�gliche Aktion des Gegners:
		for(int actionOpponent: allActionOpponent){
			
			//Platziere Stein des Gegner im Spielfeld
			int[][] nextStatePlayer = Helper.deepCopy2DArray(nextStateOpponent);
			int rowOpponent = Game.placeDiskPossible(actionOpponent);
			if (playerID == 1)
				nextStatePlayer[rowOpponent][actionOpponent] = 2; 
			else 
				nextStatePlayer[rowOpponent][actionOpponent] = 1;
			
			//W�hle h�chsten Value aller m�glichen n�chsten Z�ge f�r QPlayer aus.
			double newMax = maxValueForState(nextStatePlayer);
			//int newMax = avgValueForState(nextStatePlayer);
			
			if(newMax > maxOfallPossibleActions)
				maxOfallPossibleActions = newMax;
				
		}
		
		return maxOfallPossibleActions;
	}
	/**
	 * Funktioniert �hnlich wie die Methode maxValueForNextStateAllActions, allerdings wird hier anstatt das Maximum der Durchschnitt aller Aktionen gebildet
	 * @param state
	 * @param action
	 * @return
	 */

	private double avgValueForNextStateAllActions(int[][] state, int action) {

		//Simulieren: QPlayer wirft seinen Stein - richtiges Board wird erst ver�ndert nach dem Zug
		
		// 1. hole Kopie des Spielfelds
		int[][] nextStateOpponent = Helper.deepCopy2DArray(state); 

		// 2. berechne in welcher Zeile der Stein landenw�rde
		int row = Game.placeDiskPossible(action);
		
		// 3. markiere das entsprechende Feld mit der ID des Players 
		nextStateOpponent[row][action] = playerID;
		
		
		
		//Berechne wo Gegner hinwerfen k�nnte:
		int[] allActionOpponent = generateActions(nextStateOpponent, false);
		
		//Invertiere das Array um Erfahrungsdaten-Bank auf gegnerischen Zug anwenden zu k�nnen.		
		int[][] inversIextStateOpponent = Helper.deepCopy2DArray(nextStateOpponent);
		for (int i=0; i<Game.ROWS; i++ ){
			for (int j=0; j<Game.COLUMNS; j++){
				if (inversIextStateOpponent[i][j]==1){
					inversIextStateOpponent[i][j]=2;
				}
				if (inversIextStateOpponent[i][j]==2){
					inversIextStateOpponent[i][j]=1;
				}
			}
		}
		
		// Wir gehen davon aus, dass der Gegner den f�r sich besten Zug w�hlt.
		int bestAction = allActionOpponent[0];
		Double bestValue = PUNISHMENT-1;

		if(Q.containsState(inversIextStateOpponent)){
			for(int actionOpponent: allActionOpponent){
				
				double value = Q.getValueOfStateAndAction(inversIextStateOpponent, actionOpponent);
				if(value > bestValue){
					bestValue = value;
					bestAction = action;
				}
			}
		}
		
		// Falls keine Erfahrungswere bestehen, wird eine zuf�llige Spalte gew�hlt
		else{
			int zufallszahl = (int)(Math.random() * allActionOpponent.length);
			bestAction=allActionOpponent[zufallszahl];
		}
			
		//Platziere Stein des Gegner f�r beste Aktion im Spielfeld
		int[][] nextStatePlayer = Helper.deepCopy2DArray(nextStateOpponent);
		int rowOpponent = Game.placeDiskPossible(bestAction);
		if (playerID == 1)
			nextStatePlayer[rowOpponent][bestAction] = 2; 
		else 
			nextStatePlayer[rowOpponent][bestAction] = 1;
			
		//Berechne Durchschnitt aller m�glichen Z�ge
		return avgValueForState(nextStatePlayer);
		
	}


	/**
	 *Sieht sich das aktuelle Spielfeld(State) an und gibt alle m�glichen Z�ge(Actions) zur�ck.
	 *
	 * Falls in der Datenbank Q bereits der State vorhanden ist, frage die m�glichen Actions ab und gib sie zur�ck.
	 * 
	 * Falls in Q noch nicht der State vorhanden ist, teste welche Z�ge (Actions) m�glich sind und schreibe sie anschlie�ende in die DB (mit Value 0):
	 *  
	 * @param state
	 * @param forQPlayer Wenn f�r QPlayer Action generiet werden, dann werden diese auch in Q gespeichert, ansonsten nicht
	 * @return possibleActions, oder null wenn irgendwas schief gegangen ist.
	 */
	private int[] generateActions(final int[][] state, boolean forQPlayer){
		int[] possibleActions = null;
		
		//Falls in der Datenbank Q bereits der State vorhanden ist, frage die m�glichen Actions ab und gib sie zur�cl
		if(forQPlayer && Q.containsState(state)){
			
			possibleActions = new int[Q.get(state).keySet().size()];
			int i = 0;
			for(int action : Q.get(state).keySet()){
				possibleActions[i] = action;
				i++;
			}
		}
		//Falls in Q noch nicht der State vorhanden ist, teste welche Z�ge (Actions) m�glich sind und schreibe sie anschlie�ende in die DB (mit Value 0):
		else{
			
			int[] allActions = new int[Game.COLUMNS]; 
		
			int actionCount = 0;
			for (int j = 0; j<= Game.COLUMNS-1; j++){
				for(int i = Game.ROWS -1; i >=0; i--){
					if(state[i][j] == 0){
						allActions[actionCount] = j; //wirklich j? warum dann nicht einfach obere Zeile kontrulieren?
						actionCount++;
						break;
						
					}
				}
			}
			//System.out.println(Helper.convertIntArrayToString(allActions));
			
			possibleActions = new int[actionCount];
			for(int a = 0; a <actionCount;a++){
				possibleActions[a] = allActions[a];
			}
			//Falls der QPlayer gerade ein Turnier spielt, werden keine neuen Datenbankeintr�ge gemacht
			if(learning){
				//in Datenbank Q schreiben und die Values mit 0 initialisieren, da bisher noch nicht betrachtet:
				initializeState(state, possibleActions);
			}
		}
		
		return possibleActions;
	}
	
	private int chooseBestAction(final int[][] currentState, final int[] actions){
		
		int bestAction;
		
		//F�ge Array hinzu, um bei mehrern gleich guten M�glichkeiten alle zuspeichern
		int[] bestActions=new int[actions.length];;
		int anz=0; // Anzahl der Elemente im Array
		double bestValue = PUNISHMENT-1 ; //Double.MIN_VALUE;
		int zz; //Zufallszahl f�r Spaltenauswahl

		//Innerhalb der Lern-Phase sollen epsilon% der Z�ge zuf�llig gew�hlt werden umzu vermeiden, 
		//dass sich der Q-Player fest f�hrt.
		if (learning){
			Random grn = new Random();
			zz =grn.nextInt(100)+1;
		}
		// Im Turnier sollen kene Zuf�lligen Z�ge mehr gew�hlt werden, daher wir zz auf 100 gesetz.
		else {
			zz=100;
		}
		//Q.saveDBToTxt();
		
		if(Q.containsState(currentState)){
			if (zz>=epsilon){
				for(int i=0; i<actions.length; i++){
					double value = Q.getValueOfStateAndAction(currentState, actions[i]);

					//System.out.println(actions[i] + ", value: " + value + ", bestvalue: " +bestValue);
					if(Double.compare(value, bestValue) >= 0){
						//System.out.println("Besseren Wert gefunden");

						if(value>bestValue){ 
						// verwerfe Array wenn neuer Best-Wert gefunden wurde
							bestActions = new int[actions.length];
							anz=0;
						}
							
							bestValue = value;
							bestActions[anz] = actions[i];
							anz++;
					}
				}
				//W�hle einen zuf�lligen Zug aus dem Array bestactions
				System.out.println("W�hle aus "+ anz+" M�glichkeiten");
				int zufallszahl = (int)(Math.random() * anz);
				System.out.println("W�hle M�glichkeiten Nr. " + (zufallszahl+1));
				bestAction=bestActions[zufallszahl];
				
				System.out.println(Q.get(currentState));
				if (Q.getValueOfStateAndAction(currentState, bestAction) == 0.0) anzZuegeMitWertungNulll++;

				
			}
			else {
				
				//W�hle zuf�llig weil epsilon-greedy greift 
				System.out.println("W�hle zuf�llig (epsilon)");
				int zufallszahl = (int)(Math.random() * actions.length);
				bestAction=actions[zufallszahl];
			}
		}
		
		else{
			// W�hle zuf�llig weil Zustand unbekannt 
			anzUnbekannteZustaende++;
			System.out.println("W�hle zuf�llig (Zustand unbekannt)");
			int zufallszahl = (int)(Math.random() * actions.length);
			bestAction=actions[zufallszahl];
		}
		return bestAction;
	}
	
	/**
	 * Bekommt einen State und betrachtet auf diesem State alle m�glichen Actions und die Bewertung,
	 * die sich aus dieser Action ergeben. Gibt dann den maximalen zur�ck
	 * @param state
	 * @return
	 */
	private double maxValueForState(final int[][] state){
		//max[Q(next state, all actions] //wobei next state schon der ist, der �bergeben wird
		double maxValue;
		
		if(Q.containsState(state)){
			maxValue = Double.MIN_VALUE;

			HashMap<Integer,Double> allActionAndValues = Q.get(state);
			
			for(Integer action : allActionAndValues.keySet()){
				double value = allActionAndValues.get(action);
				if(value > maxValue)
					maxValue = value;
			}
			return maxValue;
		}
	
			
		//Wenn der State noch nicht vorhanden ist, kann an Q auch nicht ver�ndert werden, also betrachte Standardwert
		
		maxValue = 0.0;
		return maxValue; //action
	}
	/**
	 * Berechnet bei einem gegebenen State, den Durchschnitt aller Values, die erreicht werden k�nnen
	 * @param state
	 * @return
	 */
	private double avgValueForState(final int[][] state){
		double avgValue = 0;
		int count = 0;
		if(Q.containsState(state)){
	
			HashMap<Integer,Double> allActionAndValues = Q.get(state);
			
			for(Integer action : allActionAndValues.keySet()){
				double value = allActionAndValues.get(action);
				avgValue += value;
				count++;
			}
			
		}
		
		if(count == 0)
			return 0;
		else
			return avgValue / count;
	}
	

	
	@Override
	public int getPlayerID() {

		return playerID;
	}
	
	public void testMethods(){
		//TestGenerateActions();
		//TestGetMaximum();
		//TestDB();
		//newTestCase();

	}

	private void newTestCase() {
		
		int[][] state = {{1,0,0,0,0},
						 {1,1,0,1,0},
						 {2,2,2,1,0},
						 {1,2,1,2,0}};
		int[] actions = generateActions(state, true);
		int action = chooseBestAction(state, actions);	
		
		
		
		
	}

	private void TestDB() {
		int[][] state = {{0,1,0,0},
						 {0,1,0,1},
						 {0,2,2,1},
						 {1,2,1,2}};
		System.out.println(Helper.convertIntBoardToString(state));
		
		Q.put(state, 0, -100.0);
		Q.put(state,2,50.0);
		Q.put(state, 3, 100.0);
		Q.saveDBToTxt();
		
		int[][] nextState = {{0,1,0,0},
							 {1,1,0,1},
							 {2,2,2,1},
							 {1,2,1,2}};
		Q.put(nextState, 0, -100);
		Q.put(nextState,2,50);
		Q.put(nextState, 3, 100);
		Q.saveDBToTxt();
		
		int[][] samestate = {{0,1,0,0},
							 {0,1,0,1},
							 {0,2,2,1},
							 {1,2,1,2}};
		System.out.println(Q.containsState(samestate));
		
		System.out.println(Arrays.equals(state, samestate));
		
		System.out.println(Helper.deepEquals2DArray(state, samestate));
		
		
		int[] arr1 = {1,2,3};
		int[] arr2 = {1,2,3};
		System.out.println("Sind 2 gleiche Arrays gleich mit Equals??" + arr1.equals(arr2));
		
		//Q.update(samestate, 0, 50);
		
		
		
		
				
	}

	private void TestGetMaximum() {
		
		QPlayer2 qp = new QPlayer2(1);
		int[][] state = {{0,1,0,0},
						 {0,1,0,1},
						 {0,2,2,1},
						 {1,2,1,2}};
		System.out.println(Helper.convertIntBoardToString(state));
		
		Q.put(state, 0, -100);
		Q.put(state,2,50);
		Q.put(state, 3, 100);
		Q.saveDBToTxt();
		double max = qp.maxValueForState(state); 
		System.out.println("Max sollte 100 sein: " + max);
		
		int bestAction = qp.chooseBestAction(state, generateActions(state, true));
		
		System.out.println("Und der beste Zug ist " + bestAction);
		
	}

	private void TestGenerateActions() {
		System.out.println("------------Teste generateActions----------- \n");
		int[][] board = {{0,1,0,0},
						 {0,1,0,1},
						 {2,2,2,1},
						 {1,2,1,2}};
		System.out.println(Helper.convertIntBoardToString(board));
		int[] actions =  generateActions(board, true);
		int[] testActions = {0,2,3};
		
		System.out.println("Diese Actions sollten zur�ckgegeben werden: " + Helper.convertIntArrayToString(testActions));
		System.out.println(Helper.convertIntArrayToString(actions));
		System.out.println(Arrays.equals(actions, testActions));
		
		System.out.println("\n---------------End Test-------------------");
		
		System.out.println("------------Teste COMPLEX generateActions----------- \n");
		int[][] board2 = {  {2,1,0,0,1,2,0},
							{2,1,0,0,1,2,1},
							{2,1,0,0,1,2,1},
							{2,1,0,0,1,2,1},
							{1,2,1,2,1,2,1}};
		System.out.println(Helper.convertIntBoardToString(board2));
		int[] actions2 =  generateActions(board2,true);
		int[] testActions2 = {2,3,6};
		
		System.out.println("Diese Actions sollten zur�ckgegeben werden: " + Helper.convertIntArrayToString(testActions2));
		System.out.println(Helper.convertIntArrayToString(actions2));
		System.out.println(Arrays.equals(actions2, testActions2));
		
		System.out.println("\n---------------End Test-------------------");
		
	}

	public boolean isLearning() {
		return learning;
	}

	public void setLearning(boolean learning) {
		this.learning = learning;
	}

	public int getAnzUnbekannteZustaende() {
		return anzUnbekannteZustaende;
	}

	public void setAnzUnbekannteZustaende(int anzUnbekannteZustaende) {
		this.anzUnbekannteZustaende = anzUnbekannteZustaende;
	}

	public int getAnzZuegeMitWertungNulll() {
		return anzZuegeMitWertungNulll;
	}

	public void setAnzZuegeMitWertungNulll(int anzZuegeMitWertungNulll) {
		this.anzZuegeMitWertungNulll = anzZuegeMitWertungNulll;
	}

}
