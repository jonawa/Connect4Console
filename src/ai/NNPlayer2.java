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

public class NNPlayer2 implements IPlayer {
	private final int playerID;
	private NeuralNetwork nn;
	private MultiLayerPerceptron nnMLPerceptron;
	private int inputLayerValue;
	private int hiddenLayerValue;
	private int outputLayerValue;
	private double maxErrorValue;
	private double learningRateValue;
	private double momentumValue;

	public NNPlayer2(int playerID) {
		this.playerID = playerID;
		learnNNPlayer();
	}

	public NNPlayer2(int playerID, int columns, int rows, int wincount) {
		this.playerID = playerID;
		learnNNPlayer(columns, rows, wincount);
	}
	
	public NNPlayer2(int playerID, int columns, int rows, int wincount, 
			int inputLayer, int hiddenLayer, int outputLayer, double maxError, double learningRate, double momentum) {
		this.playerID = playerID;
		inputLayerValue = inputLayer;
		hiddenLayerValue = hiddenLayer;
		outputLayerValue = outputLayer;
		maxErrorValue = maxError;
		learningRateValue = learningRate;
		momentumValue = momentum;
		//learnNNPlayer(columns, rows, wincount);
		
		// MultilaerPerceptron wird erstellt 60 Input Neuronen, 120 Hidden
		// Neuronen, 5 Output Neuronen
		nnMLPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputLayerValue, hiddenLayerValue, outputLayerValue);

		// LearningRule setzen
		MomentumBackpropagation learningRule = (MomentumBackpropagation) nnMLPerceptron.getLearningRule();
		learningRule.setMaxError(maxErrorValue);
		learningRule.setLearningRate(learningRateValue);
		learningRule.setMomentum(momentumValue);

		nnMLPerceptron.setLearningRule(learningRule);
		System.out.println("NNPlayer wurde erstellt.");
		
	}

	private void learnNNPlayer() {
		DataSet ds = null;
		try {
			/*
			 * Datenset muss erstellt werden und wird eingelesen, die ersten 60
			 * Elemete sind Input, die nächsten 55 Elemente sind Output getrennt
			 * sind die Daten durch ein Komma
			 */
			ds = TrainingSetImport.importFromFile("dataset3x3-3.txt", inputLayerValue, outputLayerValue, ",");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// MultilaerPerceptron wird erstellt 60 Input Neuronen, 120 Hidden
		// Neuronen, 5 Output Neuronen
		nnMLPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputLayerValue, hiddenLayerValue, outputLayerValue);

		// LearningRule setzten
		MomentumBackpropagation learningRule = (MomentumBackpropagation) nnMLPerceptron.getLearningRule();
		learningRule.setMaxError(maxErrorValue);
		learningRule.setLearningRate(learningRateValue);
		learningRule.setMomentum(momentumValue);

		nnMLPerceptron.setLearningRule(learningRule);

		// hier wird das Netz trainiert
		System.out.println("Starte lernen");
		nnMLPerceptron.learn(ds);

		System.out.println("Lernen abgeschlossen");
	}
	
	public void learnNNPlayer(int columns, int rows, int wincount) {
		DataSet ds = null;
		try {
			/*
			 * Datenset muss erstellt werden und wird eingelesen, die ersten 60
			 * Elemete sind Input, die nächsten 55 Elemente sind Output getrennt
			 * sind die Daten durch ein Komma
			 */
			ds = TrainingSetImport.importFromFile("dataset_"+columns+"x"+rows+"_"+wincount+"G.txt", inputLayerValue, outputLayerValue, ",");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// MultilaerPerceptron wird erstellt 60 Input Neuronen, 120 Hidden
		// Neuronen, 5 Output Neuronen
		nnMLPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, inputLayerValue, hiddenLayerValue, outputLayerValue);

		// LearningRule setzten
		MomentumBackpropagation learningRule = (MomentumBackpropagation) nnMLPerceptron.getLearningRule();
		learningRule.setMaxError(maxErrorValue);
		learningRule.setLearningRate(learningRateValue);
		learningRule.setMomentum(momentumValue);

		nnMLPerceptron.setLearningRule(learningRule);

		// hier wird das Netz trainiert
		System.out.println("Starte lernen");
		nnMLPerceptron.learn(ds);

		System.out.println("Lernen abgeschlossen");
	}

	
	public void learnAndSaveNNPlayer() {
		DataSet ds = null;
		try {
			/*
			 * Datenset muss erstellt werden und wird eingelesen, die ersten 60
			 * Elemete sind Input, die nächsten 55 Elemente sind Output getrennt
			 * sind die Daten durch ein Komma
			 */
			ds = TrainingSetImport.importFromFile("dataset_"+Game.COLUMNS+"x"+Game.ROWS+"_"+Game.WINCOUNT+"G.txt", inputLayerValue, outputLayerValue, ",");

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// hier wird das Netz trainiert
		System.out.println("Starte lernen");
		nnMLPerceptron.learn(ds);

		System.out.println("Lernen abgeschlossen");
		
		// save the trained network into file
		nnMLPerceptron.save("nnPlayer2_learned.nnet");
		
		System.out.println("NNPlayer wurde gespeichert.");
	}
	
	@Override
	public int turn() {
		int action = 0;
		
		nnMLPerceptron.load("NNPlayer2_learned.nnet");
		
		// Get current board
		int[][] currentBoard = Helper.deepCopy2DArray(Game.getBoard());

		// Generate output to calculate the next move
		double[] input = Helper.convertIntBoardToDoubleArray(currentBoard);
		nnMLPerceptron.setInput(input);
		nnMLPerceptron.calculate();

		double[] netOutput = nnMLPerceptron.getOutput();
		System.out.println(Arrays.toString(netOutput));

		// Turn output into an action for the next move
		action = getMax(netOutput);

		// Check output
		while (!isActionAllowed(currentBoard, action)) {
			for (int i = 2; i < Game.COLUMNS; i++) {
				action = getNextMax(netOutput, i);
			}
		}

		System.out.println(action);
		return action;
	}

	private int getNextMax(double[] array, int count) {
		Arrays.sort(array);
		return (int) array[array.length-count];
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
			if (array[i] > max) {
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
