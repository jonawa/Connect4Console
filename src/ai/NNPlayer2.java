package ai;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TrainingSetImport;
import org.neuroph.util.TransferFunctionType;

import main.Game;
import main.IPlayer;
import util.Helper;
import util.TrainNNetwork;

public class NNPlayer2 implements IPlayer{
	private final int playerID;
	private NeuralNetwork nn;
	private MultiLayerPerceptron myMlPerceptron;
	
	public NNPlayer2(int playerID) {
		this.playerID = playerID;
		learnNNPlayer();
		
		
	}
	
	public void learnNNPlayer(){
		DataSet ds = null;
		try {
			 ds = TrainingSetImport.importFromFile("dataset.txt", 9, 3, ",");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	
		myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 9, 6, 3);

        MomentumBackpropagation learningRule = (MomentumBackpropagation) myMlPerceptron.getLearningRule();
        learningRule.setMaxError(0.15);
        learningRule.setLearningRate(0.2);
        learningRule.setMomentum(0.7);

		myMlPerceptron.setLearningRule(learningRule);
		System.out.println("Starte lernen");
		myMlPerceptron.learn(ds);
		
		System.out.println("Lernen abgeschlossen");
		}

	


	@Override
	public int turn() {
		int action = 0;
		//Get current board
		int[][] currentBoard = Helper.deepCopy2DArray(Game.getBoard());
		
		//Generate output to calculate the next move
		double[] input = Helper.convertIntBoardToDoubleArray(currentBoard);
		myMlPerceptron.setInput(input);
        myMlPerceptron.calculate();
       
        double[ ] netOutput =  myMlPerceptron.getOutput();
        System.out.println(Arrays.toString(netOutput));
		
        //Turn output into an action for the next move
        action = getMax(netOutput);
        System.out.println(action);
		return action;
	}
	
	private int getMax(double[] array) {
		double max = 0;
		int row = 0;
		for (int i = 0; i < array.length; i++) {
			if(array[i]> max){
				max = array[i];
				row = i;
			}
				
		}
		
		return row;
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
