package ai;

import org.neuroph.core.NeuralNetwork;

import main.IPlayer;

public class NNPlayer2 implements IPlayer{
	private final int playerID;
	private NeuralNetwork nn;
	
	public NNPlayer2(int playerID) {
		this.playerID = playerID;
		nn = NeuralNetwork.load("NewNeuralNetwork1.nnet");
		
		
		
	}
	@Override
	public int turn() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reactToWinOrLose(boolean win) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPlayerID() {
		// TODO Auto-generated method stub
		return playerID;
	}
	@Override
	public void setLearning(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	

}
