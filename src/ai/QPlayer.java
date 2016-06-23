package ai;

import db.TestDB;
import main.Game;
import main.IPlayer;

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
	
	@Override
	public int turn() {
		//Wie sieht das Spielfeld gerade aus:
		int[][] currentState = Game.getBoard();
		
		
			//aus allen MöglichenActions, die beste.. oder eine zufällig auswählen
			int[] actions = generateActions(currentState);
			int action = chooseBestAction(actions);
			
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
		return null;
		
	}
	
	private int[] isEndState(final int[][] state){
		return null;
	}
	
	
	private int chooseBestAction(final int[] actions){
		return 0;
	}
	
	/**
	 * Bekommt einen State und betrachtet auf diesem State alle möglichen Actions und die Bewertung,
	 * die sich aus dieser Action ergeben. Gibt dann den maximalen zurück
	 * @param state
	 * @return
	 */
	private int maximimumOfallPossibleActions(int[][] state){
		//max[Q(next state, all actions] //wobei next state schon der ist, der übergeben wird
		
		return 0; //action
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
		Q.update(currentState, action, 1000);
		
	}

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
