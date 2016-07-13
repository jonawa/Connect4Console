package util;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class TrainNNetwork {

	public TrainNNetwork() {
		// TODO Auto-generated constructor stub
	}

//	public static void main(String[] args) {
//		// create training set
//        DataSet trainingSet = new DataSet(60, 5);
//        //trainingSet.addRow(new DataSetRow(methUmwandlung(), outputMoeglicheZustaende());
//        
//
//        // create multi layer perceptron
//        MultiLayerPerceptron NNMlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 60, 60, 5);
//        
//        // learn the training set
//        NNMlp.learn(trainingSet);
//
//        // test perceptron
//        System.out.println("Testing trained neural network");
//        testNeuralNetwork(NNMlp, trainingSet);
//
//        // save trained neural network
//        NNMlp.save("NNMlp.nnet");
//
//        // load saved neural network
//        NeuralNetwork loadedMlp = NeuralNetwork.createFromFile("NNMlp.nnet");
//
//        // test loaded neural network
//        System.out.println("Testing loaded neural network");
//        testNeuralNetwork(loadedMlp, trainingSet);
//
//    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

        for(DataSetRow dataRow : testSet.getRows()) {

            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[ ] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
            System.out.println(" Output: " + Arrays.toString(networkOutput) ); 

        }

    }
    
    public static double[] convertBoard(int[][] board) {
    	double[] converted = new double [board.length * board[0].length * 3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                switch (board [i][j]){
                    case 0 :    
                        converted [(i * board[0].length *3) + (j*3)    ] = 1;
                        converted [(i * board[0].length *3) + (j*3) + 1] = 0;
                        converted [(i * board[0].length *3) + (j*3) + 2] = 0;
                        break;
                    case 1 :
                        converted [(i * board[0].length *3) + (j*3)    ] = 0;
                        converted [(i * board[0].length *3) + (j*3) + 1] = 1;
                        converted [(i * board[0].length *3) + (j*3) + 2] = 0;
                        break;
                    case 2 :
                        converted [(i * board[0].length *3) + (j*3)    ] = 0;
                        converted [(i * board[0].length *3) + (j*3) + 1] = 0;
                        converted [(i * board[0].length *3) + (j*3) + 2] = 1;
                        break;
                }
            }
        }
    	return converted;
    }
    
    public static void main(String[] args) {
		int[][] test = {{0,2},{2,1}};
		double[] arr = convertBoard(test);
		System.out.println(Arrays.toString(arr));
	}
    
}
