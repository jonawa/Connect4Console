/**
 * 
 */
package ai;

import java.util.Arrays;
import main.IPlayer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;


/**
 * @author Lena
 *
 */
public class NNPlayer implements IPlayer {

	private final int playerID;
	
	/**
	 * Generates an AI Player for the game Connect Four 
	 * which uses a neural network to react to the move of the enemy.
	 * 
	 * @param playerID 1 for Player 1 or 2 for Player 2
	 */
	public NNPlayer(int playerID) {
		this.playerID = playerID;
	}

	/* (non-Javadoc)
	 * @see main.IPlayer#turn()^^
	 */
	@Override
	public int turn() {
		int action = 0;
		
		// TODO Hier soll NN die naechste action waehlen.
		
		//NN load()
		//NN -> Test mit 1 Zustand (aktuelles Spielfeld)
		
		return action;
	}

	/* (non-Javadoc)
	 * @see main.IPlayer#reactToWinOrLose(boolean)
	 */
	@Override
	public void reactToWinOrLose(boolean win) {
		// not required?
	}
	
	// erstmal zum testen, spaeter aendern!
	public static void main(String[] args) {

        // create training set
        DataSet trainingSet = new DataSet(60, 5);
        //trainingSet.addRow(new DataSetRow(methUmwandlung(), outputMoeglicheZustaende());
        

        // create multi layer perceptron
        MultiLayerPerceptron NNMlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 60, 60, 5);
        
        // learn the training set
        NNMlp.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(NNMlp, trainingSet);

        // save trained neural network
        NNMlp.save("NNMlp.nnet");

        // load saved neural network
        NeuralNetwork loadedMlp = NeuralNetwork.createFromFile("NNMlp.nnet");

        // test loaded neural network
        System.out.println("Testing loaded neural network");
        testNeuralNetwork(loadedMlp, trainingSet);

    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

        for(DataSetRow dataRow : testSet.getRows()) {

            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) ); 

        }

    }
	
	/* (non-Javadoc)
	 * @see main.IPlayer#getPlayerID()
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}

}
