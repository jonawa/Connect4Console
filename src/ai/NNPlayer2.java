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
	private MultiLayerPerceptron myMlPerceptron;
	
	/**
	 * Erzeugt einen KI-Spieler für das Spiel "Vier Gewinnt", welcher ein Neurnales Netz nutz,
	 * um auf die Zuege des Gegners zu reagieren.
	 * 
	 * Achtung: Hier muessen zum Lernen oder Laden die entsprechenden Zeilen auskommentiert werden!
	 * 
	 * @param playerID   1 fuer Spieler 1 oder 2 fuer Spieler 2
	 */
	public NNPlayer2(int playerID) {
		this.playerID = playerID;
		
		/*
		 * Neuronales Netz erstellen und lernen lassen:
		 * -> Hier auskommentieren, wenn vorhandenes Netz geladen werden soll!
		 */
		//learnNNPlayer();
		
		/*
		 * Gespeichertes Netz laden:
		 * Hier immer den Parameter fuer das zu ladenene Neuronale Netz anpassen!
		 * -> Hier auskommentieren, wenn ein neues Netz erstellt werden soll!
		 */
		myMlPerceptron  = (MultiLayerPerceptron) MultiLayerPerceptron.load("NNPlayer1_HL240_200.nnet");
		System.out.println("NN geladen.");

	}
	
	/**
	 * Methode erzeugt ein neues Neuronales Netz anhand einer Textdatei und den dazu passenden Parametern. 
	 *
	 */
	public void learnNNPlayer(){
		DataSet ds = null;
		try {
			 /*
			  * Datenset muss vorher erstellt worden sein und wird hier eingelesen. 
			  * Fuer ein Spielfeld 4x5 sind die ersten 60 Elemete Input-Werte, die nächsten 5 Elemente Output-Werte,
			  * welche alle durch ein Komma getrennt werden. 
			  * */
			 ds = TrainingSetImport.importFromFile("dataset_5x4_3G_p1_400.txt", 60, 5, ",");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* 
		* MultilayerPerceptron erstellen.
		* 60 Input Neuronen, 120 Hidden Neuronen, 5 Output Neuronen fuer Spielfeld 4x5 als Beispiel.
		* 
		* Beim Erstellen eines neuen Neuronalen Netzes muessen die Parameter entsprechend angepasst werden!
		*/
		myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 60, 240, 5);


		/*
		 * LearningRule setzen.
		 * Hier koennen die Parameter für den maximalen Fehler, die Lernrate und das Momentum entsprechend veraendert werden.
		 */
        MomentumBackpropagation learningRule = (MomentumBackpropagation) myMlPerceptron.getLearningRule();
        learningRule.setMaxError(0.01);
        learningRule.setLearningRate(0.2);
        learningRule.setMomentum(0.7);
        
        //Angepasste learningRule an das MultiLayerPerceptron uebergeben.
		myMlPerceptron.setLearningRule(learningRule);
		
		//Netz trainieren (Lernen).
		System.out.println("Starte lernen");
		myMlPerceptron.learn(ds);	
		System.out.println("Lernen abgeschlossen");
		
		//Netz speichern
		myMlPerceptron.save("NNPlayer1_HL240_400.nnet");
		System.out.println("NNPlayer wurde gespeichert!");
		}

	

	/* (non-Javadoc)
	 * @see main.IPlayer#turn()^^
	 */
	@Override
	public int turn() {
		int action = 0;
		//Das aktuelle Spielfeld
		int[][] currentBoard = Helper.deepCopy2DArray(Game.getBoard());
		
		/*
		 * Um den naechsten Zug generieren zu koennen, muss currentBoard in Input fuer das Neuronale Netz umgewandelt werden.
		 */
		double[] input = TrainNNetwork.convertBoard(currentBoard);
		
		// Input an Neuronale Netz fuer Kalkulation uebergeben.
		myMlPerceptron.setInput(input);
        myMlPerceptron.calculate();
       
        // Output erzeugen
        double[] netOutput =  myMlPerceptron.getOutput();
		
        // Output umwandeln in Aktion fuer den naechsten Zug (action).
     	action = getMax(netOutput);
        System.out.println(Arrays.toString(netOutput));
		
		// Ueberpruefung, ob action fuer den naechsten Zug erlaubt ist.
		while (!isActionAllowed(currentBoard, action)) {
			action = getNextMax(netOutput, action);
		}
		
		return action;
	}

	/**
	 * Diese Methode gibt den naechst hoechsten Wert des Output-Arrays zurueck.
	 * 
	 * @param Output-Array
	 * @param zu pruefende Aktion
	 * @return Naechst hoechster Wert im Output-Array
	 */
	private int getNextMax(double[] array, int action) {
		double Max = array [action];
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

	/**
	 * Diese Methode prueft, ob Aktion erlaubt ist.
	 * 
	 * @param Spielfeld
	 * @param zu pruefende Aktion
	 * @return true, wenn Aktion erlaubt, ansonsten false
	 */
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

	/**
	 * Diese Methode berechnet das Maximum in einem Array.
	 * 
	 * @param array
	 * @return Maximum
	 */
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
		// Fuer NNPlayer nicht notwendig.	
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
		// Fuer NNPlayer nicht notwendig.
	}
	
}