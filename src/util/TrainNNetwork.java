package util;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

public class TrainNNetwork {

	public TrainNNetwork() {
		
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

    
}
