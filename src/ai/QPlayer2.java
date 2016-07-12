package ai;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import db.TestDB;
import db.TestDB2;
import db.TestDB3;
import main.Game;
import main.IPlayer;
import util.Helper;

public class QPlayer2 implements IPlayer {
	
	public static final int REWARD = 100; //wenn gewonnen
	public static final int PUNISHMENT = -100; //wenn verloren
	
	private final int playerID;
	private TestDB2 Q;
	private double gamma;
	private int epsilon;
	private boolean learning=true;
	
	
	private int[][] lastState;
	private int lastAction;
	
	public QPlayer2(int playerID) {

		this.playerID = playerID;
		Q = TestDB2.getDB();
		gamma = 0.8;
	}
	
	public static void main(String[] args) {
		QPlayer2 qplayer = new QPlayer2(1);
		qplayer.testMethods();
	}
	
	
	
	@Override
	public int turn() {
		//Wie sieht das Spielfeld gerade aus:
		int[][] currentState = Helper.deepCopy2DArray(Game.getBoard());
		
		
		//aus allen MöglichenActions, die beste.. oder eine zufällig auswählen
		int[] actions = generateActions(currentState, true);
		
		
		int action = chooseBestAction(currentState, actions);	
			
		
		//Finde neuen Wert für Q, außer es ist mein letzter Zug:
		if (Game.tokensOnField<Game.ROWS*Game.COLUMNS-2){
			int newQValue= (int) (gamma * avgValueForNextStateAllActions(currentState, action));

			//Datenbank mit neuem Wert updaten:	
			Q.update(currentState, action, newQValue);
		}
		
		
		
		/*Speichere den aktuellen State und die ausgewählte Action. Falls die KI verliert, wird sie dazu aufgefordert auf das Verlieren zu reagieren
		 * (reactToWinrOrLose).
		 * Die KI muss also wissen, was sie gemacht hat um Q zu aktualisieren und sich zu bestrafen.
		 */
		lastState = currentState;	//tiefe Kopie erstellen, wurde oben bereits gemacht
		lastAction = action;
		
		//sichert db.txt im Projektordner, zum Testen aktuell, was in der DB steht.
		//Q.saveDBToTxt(); //TODO rausnehmen und nur am Ende einmal speichern
		
		return action;
	}
	
	/**
	 * Nachdem die beste Aktion vom QPlayer ausgewählt wurde, wird diese Methode aufgerufen.
	 * Es werden die nächsten möglichen States für den QPlayer betrachet, und von dieses States die
	 * die Action gesucht mit dem höchsten Value. Dieser Value wird zurückgegeben.
	 * Da der QPlayer nicht direkt wieder dran ist, müssen auch alle unterschiedlichen Möglichkeiten für
	 * den Gegner durchgegangen werrden.
	 * 
	 * @param state Spielfeld bevor der QPlayer seinen Stein geworfen hat.
	 * @param action Zug der vom QPlayer ausgewählt worden ist.
	 * @return erfolgsversprechenster Wert, den der QPlayer aus diese State mit dieser Action erreichen kann
	 */
	private int maxValueForNextStateAllActions(int[][] state, int action) {
		
		int maxOfallPossibleActions = Integer.MIN_VALUE;
		
		//Simulieren, QPlayer wirft seinen Stein, richtiges Board wird erst verändert nach dem Zug
		int[][] nextStateOpponent = Helper.deepCopy2DArray(state); 
		int row = Game.placeDiskPossible(action);
		nextStateOpponent[row][action] = playerID;
		
		//Simulieren wo Gegner hinwerfen könnte:
		int[] allActionOpponent = generateActions(nextStateOpponent, false);
		
		//Für jede mögliche Aktion des Gegners:
		for(int actionOpponent: allActionOpponent){
			
			//Platziere Stein des Gegner im Spielfeld
			int[][] nextStatePlayer = Helper.deepCopy2DArray(nextStateOpponent);
			int rowOpponent = Game.placeDiskPossible(actionOpponent);
			if (playerID == 1)
				nextStatePlayer[rowOpponent][actionOpponent] = 2; 
			else //TODO: hier irgendwie Spieler 2 holen?
				nextStatePlayer[rowOpponent][actionOpponent] = 1;
			
			//Wähle höchsten Value aller möglichen nächsten Züge für QPlayer aus.
			int newMax = maxValueForState(nextStatePlayer);
			//int newMax = avgValueForState(nextStatePlayer);
			
			if(newMax > maxOfallPossibleActions)
				maxOfallPossibleActions = newMax;
				
		}
		
		return maxOfallPossibleActions;
	}
	/**
	 * Funktioniert ähnlich wie die Methode maxValueForNextStateAllActions, allerdings wird hier anstatt das Maximum der Durchschnitt aller Aktionen gebildet
	 * @param state
	 * @param action
	 * @return
	 */

	private int avgValueForNextStateAllActions(int[][] state, int action) {
		
		int avgOfallPossibleActions = 0;
		int count = 0;
		
		//Simulieren, QPlayer wirft seinen Stein, richtiges Board wird erst verändert nach dem Zug
		int[][] nextStateOpponent = Helper.deepCopy2DArray(state); 

		int row = Game.placeDiskPossible(action);
		nextStateOpponent[row][action] = playerID;
		
		//Simulieren wo Gegner hinwerfen könnte:
		int[] allActionOpponent = generateActions(nextStateOpponent, false);
		
		//Invertiere das Array um Erfahrungsdaten Bank auf gegnerischen Zug Anwenden zu können.
		
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
		
		
		int bestAction = allActionOpponent[0];
		int bestValue = Integer.MIN_VALUE;
		
		//Wie berahten jetzt "Wenn ich mein Gegner wäre".
		//Für jede mögliche Aktion des Gegners:
		if(Q.containsState(inversIextStateOpponent)){
			for(int actionOpponent: allActionOpponent){
				
				int value = Q.getValueOfStateAndAction(inversIextStateOpponent, actionOpponent);
				if(value > bestValue){
					bestValue = value;
					bestAction = action;
				}
			}
		}
		// Falls keine Erfahrungswere bestehen, wird eine zufällige Spalte gewählt
		else{
			int zufallszahl = (int)(Math.random() * allActionOpponent.length);
			bestAction=allActionOpponent[zufallszahl];
		}
			
		//Platziere Stein des Gegner für beste Aktion im Spielfeld
		int[][] nextStatePlayer = Helper.deepCopy2DArray(nextStateOpponent);
		int rowOpponent = Game.placeDiskPossible(bestAction);
		if (playerID == 1)
			nextStatePlayer[rowOpponent][bestAction] = 2; 
		else //TODO: hier irgendwie Spieler 2 holen?
			nextStatePlayer[rowOpponent][bestAction] = 1;
			
		//Berechne Durchschnitt aller möglichen Züge
		return avgValueForState(nextStatePlayer);
		
	}


	/**
	 *Sieht sich das aktuelle Spielfeld(State) an und gibt alle möglichen Züge(Actions) zurück.
	 *
	 * Falls in der Datenbank Q bereits der State vorhanden ist, frage die möglichen Actions ab und gib sie zurück.
	 * 
	 * Falls in Q noch nicht der State vorhanden ist, teste welche Züge (Actions) möglich sind und schreibe sie anschließende in die DB (mit Value 0):
	 *  
	 * @param state
	 * @param forQPlayer Wenn für QPlayer Action generiet werden, dann werden diese auch in Q gespeichert, ansonsten nicht
	 * @return possibleActions, oder null wenn irgendwas schief gegangen ist.
	 */
	private int[] generateActions(final int[][] state, boolean forQPlayer){
		int[] possibleActions = null;
		
		//Falls in der Datenbank Q bereits der State vorhanden ist, frage die möglichen Actions ab und gib sie zurücl
		if(forQPlayer && Q.containsState(state)){
			
			possibleActions = new int[Q.get(state).keySet().size()];
			int i = 0;
			for(int action : Q.get(state).keySet()){
				possibleActions[i] = action;
				i++;
			}
			
		}
		//Falls in Q noch nicht der State vorhanden ist, teste welche Züge (Actions) möglich sind und schreibe sie anschließende in die DB (mit Value 0):
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
			
			//in Datenbank Q schreiben und die Values mit 0 initialisieren, da bisher noch nicht betrachtet:
			initializeState(state, possibleActions);
		}
		
		return possibleActions;
		
	}
	
	
	//Funktioniert nicht das checkWin nur das Board in Game prüft und nicht einen beliebigen State
	private boolean isEndState(final int[][] state, int action){
		
		int column = action;
		int row = Game.placeDiskPossible(column);
		
		//TODO ckeckWin anpassen		
		return Game.checkWin(playerID, row, column);

	}
	
	
	private int chooseBestAction(final int[][] currentState, final int[] actions){
		
		//TODO hier könnte man außerdem mit der isEndState Methode überprüfen, ob man direkt gewinnen kann
		//Starte mit einer beliebigen Action, hier wird immer die erstmögliche ausgewählt.
		int bestAction = actions[0];
		int bestValue = Integer.MIN_VALUE;
		
		if (learning){
			Random grn = new Random();
			epsilon=grn.nextInt(100)+1;
		}
		else {
			epsilon=100;
		}
		
		if(Q.containsState(currentState) && epsilon>20){
			for(int action : actions){
				int value = Q.getValueOfStateAndAction(currentState, action);
				if(value > bestValue){
					bestValue = value;
					bestAction = action;
				}
			}			
		}
		// Falls keine Erfahrungswere bestehen, wird eine zufällige Spalte gewählt
		else{
			System.out.println("Wähle zufällig");
			int zufallszahl = (int)(Math.random() * actions.length);
			bestAction=actions[zufallszahl];
		}
		return bestAction;
	}
	
	/**
	 * Bekommt einen State und betrachtet auf diesem State alle möglichen Actions und die Bewertung,
	 * die sich aus dieser Action ergeben. Gibt dann den maximalen zurück
	 * @param state
	 * @return
	 */
	private int maxValueForState(final int[][] state){
		//max[Q(next state, all actions] //wobei next state schon der ist, der übergeben wird
		int maxValue;
		
		if(Q.containsState(state)){
			maxValue = Integer.MIN_VALUE;

			HashMap<Integer,Integer> allActionAndValues = Q.get(state);
			
			for(Integer action : allActionAndValues.keySet()){
				int value = allActionAndValues.get(action);
				if(value > maxValue)
					maxValue = value;
			}
			return maxValue;
		}
	
			
		//Wenn der State noch nicht vorhanden ist, kann an Q auch nicht verändert werden, also betrachte Standardwert
		
		maxValue = 0;
		return maxValue; //action
	}
	/**
	 * Berechnet bei einem gegebenen State, den Durchschnitt aller Values, die erreicht werden können
	 * @param state
	 * @return
	 */
	private int avgValueForState(final int[][] state){
		int avgValue = 0;
		int count = 0;
		if(Q.containsState(state)){

	
			HashMap<Integer,Integer> allActionAndValues = Q.get(state);
			
			for(Integer action : allActionAndValues.keySet()){
				int value = allActionAndValues.get(action);
				avgValue += value;
				count++;
			}
			
		}
		
		if(count == 0)
			return 0;
		else
			return avgValue / count;
	}
	
	/**
	 * Da nicht alle Möglichkeiten zu gewinnen und zu verlieren in einer Matrix bewertet werden.
	 * Gibt diese Methode den Reward zurück mit Hilfe der checkWin Methode
	 * Also return 100, wenn gewonnen.
	 * @param state
	 * @return
	 */
//wird erstmal nicht gebraucht, da Reward erst nach dem Spielende über die Methode reactToWinOrLose verteilt wird.
	private int getReward(int[][] state, int action){
		if(isEndState(state, action))
			return REWARD;
		
		return 0;
	}
	
	/**
	 * Sobald der Algo an einem Punkt angelangt ist, an dem er die nächsten möglichen Züge noch nicht in seiner Datenbank hat (Am Anfang ist das immer der Fall)
	 * wird diese Methode ausgeführt.
	 * 
	 * Die Methode generiert alle möglichen Züge (actions) basierend auf dem aktuellen Spielfeld (state) und fügt für diesen State dann alle möglichen 
	 * Actions mit Value 0 in die Datenbank ein.
	 * 
	 * @param state Spielfeld
	 * @param possibleActions 
	 */
	public void initializeState(int[][] state, int[] possibleActions){
		
		for(int action : possibleActions){
			Q.put(state, action, 0);
			
		}
	}
	
	 
	

	@Override
	public void reactToWinOrLose(boolean win) {
		//teste ob der letzte State nicht der aktuelle State ist und wirklich eine tiefe Kopie erstellet worden ist:
		//TODO Rausnehmen, da unnötige Leistung
		if(Arrays.deepEquals(Game.getBoard(),lastState)){
			throw new RuntimeException("Die Q KI hat sich nicht den letzten Spielstand gemerkt.");
		}	
		//wenn gewonnen, muss nichts passieren, KI hat sich schon selbst belohnt, als der Gewinnzug ausgeführt wurde( mit REWARD)
		if(win){
			Q.update(lastState,lastAction,REWARD);
		}
		else{
			Q.update(lastState, lastAction, PUNISHMENT);	
		}
		Q.saveDBToTxt();
		// Q.update(currentState, action, 1000);
		
	}
	

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}
	
	public void testMethods(){
		//TestGenerateActions();
		//TestGetMaximum();
		//TestDB();
		newTestCase();

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
		
		Q.put(state, 0, -100);
		Q.put(state,2,50);
		Q.put(state, 3, 100);
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
		int max = qp.maxValueForState(state); 
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
		
		System.out.println("Diese Actions sollten zurückgegeben werden: " + Helper.convertIntArrayToString(testActions));
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
		
		System.out.println("Diese Actions sollten zurückgegeben werden: " + Helper.convertIntArrayToString(testActions2));
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

}
