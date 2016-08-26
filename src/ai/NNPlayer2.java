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
	private MultiLayerPerceptron myMlPerceptron;
	
	public NNPlayer2(int playerID) {
		this.playerID = playerID;
		//learnNNPlayer();
		
		//Gespeichertes Netz laden
		myMlPerceptron  = (MultiLayerPerceptron) MultiLayerPerceptron.load("NNPlayer_Save1.nnet");
		System.out.println("NN geladen.");
	}
	
	public void learnNNPlayer(){
		DataSet ds = null;
		try {
			 /*Datenset muss erstellt werden und wird eingelesen, die ersten 60 Elemete sind Input, die nächsten 5 Elemente sind Output,
			  * getrennt sind die Daten durch ein Komma */
			 ds = TrainingSetImport.importFromFile("dataset.txt", 60, 5, ",");
			
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
		
		//MultilayerPerceptron wird erstellt 60 Input Neuronen, 120 Hidden Neuronen, 5 Output Neuronen
		myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 60, 120, 5);

		//LearningRule setzen
        MomentumBackpropagation learningRule = (MomentumBackpropagation) myMlPerceptron.getLearningRule();
        learningRule.setMaxError(0.01);
        learningRule.setLearningRate(0.2);
        learningRule.setMomentum(0.7);
        
		myMlPerceptron.setLearningRule(learningRule);
		
		//Netz trainieren
		System.out.println("Starte lernen");
		myMlPerceptron.learn(ds);
		
		System.out.println("Lernen abgeschlossen");
		
		//Netz speichern
		myMlPerceptron.save("NNPlayer_Save1.nnet");
		System.out.println("NNPlayer wurde gespeichert!");
		}

	


	@Override
	public int turn() {
		int action = 0;
		//Get current board
		int[][] currentBoard = Helper.deepCopy2DArray(Game.getBoard());
		
		//Generate output to calculate the next move
		double[] input = TrainNNetwork.convertBoard(currentBoard);
		myMlPerceptron.setInput(input);
        myMlPerceptron.calculate();
       
        double[] netOutput =  myMlPerceptron.getOutput();
		
        
        // Turn output into an action for the next move
     	action = getMax(netOutput);
     	
        System.out.println(Arrays.toString(netOutput));
		
		// Check output
		while (!isActionAllowed(currentBoard, action)) {
			action = getNextMax(netOutput, action);
		}

		return action;
	}

	private int getNextMax(double[] array, int count) {
		double Max = array [count];
	    double[] arrCopy = Arrays.copyOf(array, array.length);
	    Arrays.sort(arrCopy);
	    double nextMax = 0;
	    for(int i= 0 ; i < arrCopy.length;i++)
	    	if(arrCopy[i] == Max){
	    		nextMax = arrCopy [i-1];
	            break;
	    	}
	    for(int j= 0 ; j < array.length;j++)
	    	if(array[j] == nextMax){
	    		return j;
	           }
	    return -1;		
	}

	private boolean isActionAllowed(int[][] board, int action) {
		int emptyRow = 0;
		for (int i = Game.ROWS - 1; i >= 0; i--) {
			if (board[i][action] == 0) {
				emptyRow = i;
			} else {
				emptyRow = -1;
			}
		}
		if (emptyRow == -1) {
			return false;
		}
		return true;
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
