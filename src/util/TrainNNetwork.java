package util;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class TrainNNetwork {

	public TrainNNetwork() {
		// TODO Auto-generated constructor stub
	}

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
    
    public static double[] convertBoardToInput(int[][] board) {
    	double[] input = new double[60];
    	
    	// TODO Umwandlung
    	
    	return input;
    }

}
