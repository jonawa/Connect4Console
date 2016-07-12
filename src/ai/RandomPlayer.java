package ai;

import main.Game;
import main.IPlayer;

public class RandomPlayer implements IPlayer {
	
	private final int playerID;

	@Override
	public int turn() {
	
		int[][] state = Game.getBoard();
		
		int[] allActions = new int[state[0].length]; 
	
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

		int []possibleActions = new int[actionCount];
		for(int a = 0; a <actionCount;a++){
			possibleActions[a] = allActions[a];
		}
		
		
		int zufallszahl = (int)(Math.random() * possibleActions.length);
    	int randomAction = possibleActions[zufallszahl];
    	
    	
		return randomAction;
	}

	@Override
	public void reactToWinOrLose(boolean win) {
		// do nothing
		
	}

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}
	
	public RandomPlayer(int playerID) {
		this.playerID  = playerID;
		
	}

}
