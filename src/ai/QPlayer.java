package ai;

import java.util.Arrays;
import java.util.HashMap;

import db.TestDB;
import main.Game;
import main.IPlayer;
import util.Helper;

public class QPlayer implements IPlayer {
	
	private final int playerID;
	private TestDB Q;
	double gamma;
	
	public QPlayer(int playerID) {
		// TODO Auto-generated constructor stub
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
		int[][] currentState = Game.getBoard();
		
		
			//aus allen MöglichenActions, die beste.. oder eine zufällig auswählen
			int[] actions = generateActions(currentState);
			int action = chooseBestAction(currentState, actions);
			
			//Betrachte den State, der aus der Action die oben gewählt wurde folgt:
			int[][] nextState = null; //TODO generate nextState
			
			
			int newQValue = (int) (getReward(currentState, action) + gamma * maximimumOfallPossibleActions(nextState));
			
			//TODO Wo baut man die isEndStateFunktion ein, bzw. braucht man die überhaupt?
			//Ne die getReward Funktion muss auch irgendwie angepasst werden eigentlich braucht man das gar nicht.
			
			Q.update(currentState, action, newQValue);
		
		
		
		
		
		return 0;
	}
	
	/**
	 *Sieht sich das aktuelle Spielfeld(State) an und gibt alle möglichen Züge(Actions) zurück   
	 * @return
	 */
	private int[] generateActions(final int[][] state){
		
		int[] allActions = new int[state.length];
	
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
		
		int[] possibleActions = new int[actionCount];
		for(int a = 0; a <actionCount;a++){
			possibleActions[a] = allActions[a];
		}
		
		return possibleActions;
		
	}
	
	private int[] isEndState(final int[][] state){
		return null;
	}
	
	
	private int chooseBestAction(final int[][] currentState, final int[] actions){
		//TODO Randomize here
		//Starte mit einer beliebigen Action
		int bestAction = actions[0];
		int bestValue = 0;
		
		
		for(int action : actions){
			int value = Q.getValueOfStateAndAction(currentState, action);
			if(value > bestValue){
				bestValue = value;
				bestAction = action;
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
		
		HashMap<Integer,Integer> allActionAndValues = Q.get(state);
		int maxValue = 0;
		
		for(Integer action : allActionAndValues.keySet()){
			int value = allActionAndValues.get(action);
			if(value > maxValue)
				maxValue = value;
		}
		
		return maxValue; //action
	}
	
	/**
	 * Da nicht alle Möglichkeiten zu gewinnen und zu verlieren in einer Matrix bewertet werden.
	 * Gibt diese Methode den Reward zurück mit Hilfe der checkWin Methode
	 * Also z.B. return 100, wenn gewonnen. oder -100, wenn verloren 
	 * @param state
	 * @return
	 */
	private int getReward(int[][] state, int action){
		return 0;
	}
	
	 
	

	@Override
	public void reactToWinOrLose(boolean win) {
		
		
		// Q.update(currentState, action, 1000);
		
	}
	

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}
	
	public void testMethods(){
		//TestGenerateActions();
		TestGetMaximum();

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
