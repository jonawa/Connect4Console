package ai;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import db.TestDB;
import main.Game;
import main.IPlayer;
import util.Helper;

public class QPlayer implements IPlayer {
	
	public static final int REWARD = 100; //wenn gewonnen
	public static final int PUNISHMENT = -100; //wenn verloren
	
	private final int playerID;
	private TestDB Q;
	private double gamma;
	
	
	private int[][] lastState;
	private int lastAction;
	
	public QPlayer(int playerID) {

		this.playerID = playerID;
		Q = TestDB.getDB();
		gamma = 0.8;
	}
	
	public static void main(String[] args) {
		QPlayer qplayer = new QPlayer(1);
		qplayer.testMethods();
	}
	@Override
	public int turn() {
		//Wie sieht das Spielfeld gerade aus:
		int[][] currentState = Helper.deepCopy2DArray(Game.getBoard());
		
		
			//aus allen MöglichenActions, die beste.. oder eine zufällig auswählen
			int[] actions = generateActions(currentState);
			int action = chooseBestAction(currentState, actions);
			
			//Betrachte den State, der aus der Action die oben gewählt wurde folgt:
			
			
			int newQValue;
			

			//TODO Den nächsten Zustand generieren:
			int[][] nextState = Helper.deepCopy2DArray(currentState); 
			int row = Game.placeDiskPossible(action);
			nextState[row][action] = playerID;
				
				
			newQValue= (int) (gamma * maximimumOfallPossibleActions(nextState));
		

			
			Q.update(currentState, action, newQValue);
		
		
		
		/*Speichere den aktuellen State und die ausgewählte Action. Falls die KI verliert, wird sie dazu aufgefordert auf das Verlieren zu reagieren
		 * (reactToWinrOrLose).
		 * Die KI muss also wissen, was sie gemacht hat um Q zu aktualisieren und sich zu bestrafen.
		 */
		lastState = currentState;	//tiefe Kopie erstellen, wurde oben bereits gemacht
		lastAction = action;
		
		Q.saveDBToTxt();
		
		return action;
	}
	
	/**
	 *Sieht sich das aktuelle Spielfeld(State) an und gibt alle möglichen Züge(Actions) zurück.
	 *
	 * Falls in der Datenbank Q bereits der State vorhanden ist, frage die möglichen Actions ab und gib sie zurück.
	 * 
	 * Falls in Q noch nicht der State vorhanden ist, teste welche Züge (Actions) möglich sind und schreibe sie anschließende in die DB (mit Value 0):
	 * 
	 * @return possibleActions, oder null wenn irgendwas schief gegangen ist.
	 */
	private int[] generateActions(final int[][] state){
		int[] possibleActions = null;
		
		//Falls in der Datenbank Q bereits der State vorhanden ist, frage die möglichen Actions ab und gib sie zurücl
		if(Q.containsState(state)){
			
			possibleActions = new int[Q.get(state).keySet().size()];
			int i = 0;
			for(int action : Q.get(state).keySet()){
				possibleActions[i] = action;
				i++;
			}
			
		}
		//Falls in Q noch nicht der State vorhanden ist, teste welche Züge (Actions) möglich sind und schreibe sie anschließende in die DB (mit Value 0):
		else{
			int[] allActions = new int[state.length]; //TODO Anzahl der Zeilen Länge oder? Es geht aber um die Spalten, hier vielleicht ein Fehler deswegen?
		
			int actionCount = 0;
			for (int j = 0;j <= state[0].length-1;j++){
				for(int i = state.length -1; i >=0 ;i--){
					if(state[i][j] == 0){
						allActions[actionCount] = j;
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
	
	private boolean isEndState(final int[][] state, int action){
		
		int column = action;
		int row = Game.placeDiskPossible(column);
		
				
		return Game.checkWin(playerID, row, column);

	}
	
	
	private int chooseBestAction(final int[][] currentState, final int[] actions){
		//TODO Randomize here
		//TODO hier könnte man außerdem mit der isEndState Methode überprüfen, ob man direkt gewinnen kann
		//Starte mit einer beliebigen Action
		int bestAction = actions[0];
		int bestValue = 0;//Integer.MIN_VALUE;
		
		if(Q.containsState(currentState)){
			
			for(int action : actions){
				int value = Q.getValueOfStateAndAction(currentState, action);
				if(value > bestValue){
					bestValue = value;
					bestAction = action;
				}
			}
		}
		return bestAction;
	}
	
	/**
	 * Bekommt einen State und betrachtet auf diesem State alle möglichen Actions und die Bewertung,
	 * die sich aus dieser Action ergeben. Gibt dann den maximalen zurück
	 * @param state
	 * @return
	 */
	private int maximimumOfallPossibleActions(final int[][] state){
		//max[Q(next state, all actions] //wobei next state schon der ist, der übergeben wird
		int maxValue = 0;
		
		if(Q.containsState(state)){
			HashMap<Integer,Integer> allActionAndValues = Q.get(state);
			
			for(Integer action : allActionAndValues.keySet()){
				int value = allActionAndValues.get(action);
				if(value > maxValue)
					maxValue = value;
			}
		}
	
			//Wenn der State noch nicht vorhanden ist in der DB: Initialisiere alle Actions mit Value = 0
			//initializeState(state); Erstmal rausgenommen, es reicht wahrscheinlich, wenn nur in der generateActions Methode Züge generiert werden.

		
		
		return maxValue; //action
	}
	
	/**
	 * Da nicht alle Möglichkeiten zu gewinnen und zu verlieren in einer Matrix bewertet werden.
	 * Gibt diese Methode den Reward zurück mit Hilfe der checkWin Methode
	 * Also return 100, wenn gewonnen.
	 * @param state
	 * @return
	 */
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
		TestDB();

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
				
	}

	private void TestGetMaximum() {
		
		QPlayer qp = new QPlayer(1);
		int[][] state = {{0,1,0,0},
						 {0,1,0,1},
						 {0,2,2,1},
						 {1,2,1,2}};
		System.out.println(Helper.convertIntBoardToString(state));
		
		Q.put(state, 0, -100);
		Q.put(state,2,50);
		Q.put(state, 3, 100);
		Q.saveDBToTxt();
		int max = qp.maximimumOfallPossibleActions(state); 
		System.out.println("Max sollte 100 sein: " + max);
		
		int bestAction = qp.chooseBestAction(state, generateActions(state));
		
		System.out.println("Und der beste Zug ist " + bestAction);
		
	}

	private void TestGenerateActions() {
		System.out.println("------------Teste generateActions----------- \n");
		int[][] board = {{0,1,0,0},
						 {0,1,0,1},
						 {2,2,2,1},
						 {1,2,1,2}};
		System.out.println(Helper.convertIntBoardToString(board));
		int[] actions =  generateActions(board);
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
		int[] actions2 =  generateActions(board2);
		int[] testActions2 = {2,3,6};
		
		System.out.println("Diese Actions sollten zurückgegeben werden: " + Helper.convertIntArrayToString(testActions2));
		System.out.println(Helper.convertIntArrayToString(actions2));
		System.out.println(Arrays.equals(actions2, testActions2));
		
		System.out.println("\n---------------End Test-------------------");
		
	}

}
