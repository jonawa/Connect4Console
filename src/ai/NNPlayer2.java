package ai;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

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
		myMlPerceptron = (MultiLayerPerceptron) MultiLayerPerceptron.load("MLNetworkSave.nnet");
		System.out.println("NN geladen");
	}
	
	public void learnNNPlayer(){
		DataSet ds = null;
		try {
			 /*Datenset muss erstellt werden und wird eingelesen, die ersten 60 Elemete sind Input, die nächsten 55 Elemente sind Output
			  * getrennt sind die Daten durch ein Komma */
			 ds = TrainingSetImport.importFromFile("dataset_neu.txt", 126, 7, ",");
			
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
		
		//	MultilaerPerceptron wird erstellt 60 Input Neuronen, 120 Hidden Neuronen, 5 Output Neuronen
		myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 126, 252, 7);

		//LearningRule setzten
        MomentumBackpropagation learningRule = (MomentumBackpropagation) myMlPerceptron.getLearningRule();
        learningRule.setMaxError(0.1);
        learningRule.setLearningRate(0.2);
        learningRule.setMomentum(0.7);
        
		myMlPerceptron.setLearningRule(learningRule);
		
		//hier wird das Netz trainiert
		System.out.println("Starte lernen");
//		System.out.println("To stop learning, type stop");
		myMlPerceptron.learn(ds);
//		myMlPerceptron.learnInNewThread(ds);
	
//		Scanner scanner = new Scanner(System.in);
//		String input = scanner.nextLine();
//		if(input == "stop")
//			
//			myMlPerceptron.stopLearning();
//		
		
	    
		
		//Das NeuralNetwork lokal speichern
		myMlPerceptron.save("MLNetworkSave.nnet");
		System.out.println("NN gespeichert!");
		
		System.out.println("Lernen abgeschlossen");
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
        
        //TODO hier hängt es sich irgendwo auf
//		while (!isActionAllowed(currentBoard, action)) {
//			for (int i = 2; i < Game.COLUMNS; i++) {
//				action = getNextMax(netOutput, i);
//				Arrays.
//			}
//		}
        int count = 1;
        System.out.println(action);
        while (!isActionAllowed(currentBoard, action)) {
        	action = getNextMax(netOutput, count);
        	count++;
        	System.out.println(action);
        	if(count > 7)
        		throw new RuntimeException("Wieso größer als 7 alle müssten jetzt einmal druch sein");
        
        }




		return action;
	}

	private static int getNextMax(double[] array, int count) {
		
		double[] arrCopy = Arrays.copyOf(array, array.length);
		Arrays.sort(arrCopy);
		//nächst höchster Wert
		double nextMax = arrCopy[arrCopy.length-count-1];
		//schauen an welcher Position der nächstehöchste Wert im ursprünglichen Array steht
		for(int i = 0 ; i < arrCopy.length;i++){
			if(Double.compare(array[i], nextMax) == 0)
			//if(arrCopy[i] == nextMax)
				
				//Position des nächsthöchsten Wertes zurückgeben
				return i;
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
	
//  public static void main(String[] args) {
////		
//////		double[] array = {0.1, 0.5, 0.3};
//////		double[] arrCopy = Arrays.copyOf(array, array.length);
//////		
//////		array[1] = 0.666;
//////		
//////		System.out.println(Arrays.toString(arrCopy));
//////		System.out.println(Arrays.toString(array));
//////		System.out.println(Arrays.equals(array, arrCopy));
////		
//		double[] netOutput = {0.2, 0.7114, 1.22, 1.89, 6.736, 0.161, 3.412};
//        int count = 0;
//        while (true) {
//        	int nextMax = getNextMax(netOutput, count);
//        	System.out.println(nextMax + " ist " +  netOutput[nextMax]);
//        	count++;
//        	
//        	if(count > 7)
//        		throw new RuntimeException("Wieso größer als 7 alle müssten jetzt einmal druch sein");
//        
//        }
//	}
//	
	

}
