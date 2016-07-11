/**
 * 
 */
package ai;

import main.Game;
import main.IPlayer;
import util.Helper;
import util.TrainNNetwork;

import org.neuroph.core.NeuralNetwork;


/**
 * @author Lena
 *
 */
public class NNPlayer implements IPlayer {

	private final int playerID;
	
	private NeuralNetwork trainedNNet;
	
	/**
	 * Generates an AI Player for the game Connect Four 
	 * which uses a neural network to react to the move of the enemy.
	 * 
	 * @param playerID 1 for Player 1 or 2 for Player 2
	 */
	public NNPlayer(int playerID) {
		this.playerID = playerID;
		//Load trained neural network from file
			// TODO Achtung, hier immer den richtigen Namen einfuegen!
		trainedNNet = NeuralNetwork.createFromFile("NewNeuralNetwork1.nnet");
	}

	/* (non-Javadoc)
	 * @see main.IPlayer#turn()^^
	 */
	@Override
	public int turn() {
		int action = 0;
		//Get current board
		int[][] currentBoard = Helper.deepCopy2DArray(Game.getBoard());
		
		//Generate output to calculate the next move
		trainedNNet.setInput(TrainNNetwork.convertBoardToInput(currentBoard));
        trainedNNet.calculate();
        double[ ] netOutput = trainedNNet.getOutput();
		
        //Turn output into an action for the next move
        action = getMax(netOutput);
		return action;
	}

	/* (non-Javadoc)
	 * @see main.IPlayer#reactToWinOrLose(boolean)
	 */
	@Override
	public void reactToWinOrLose(boolean win) {
		// not required?
	}
	
	/**
	 * Returns the maximum value for an array.
	 * @param array 
	 */
	private int getMax(double[] array) {
		int max = 0;
		for (double i : array) {
			max = (int) Math.max(max, i);
		}
		return max;
	}
	
	
	/* (non-Javadoc)
	 * @see main.IPlayer#getPlayerID()
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

	@Override
	public void setLearning(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
