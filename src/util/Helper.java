package util;

import java.util.Arrays;

/**
 * Helper Klasse in dem wir alles auslagern k�nnen, was wir nicht in die Main packen wollen, aber keine eigene Klasse rechtfertigt.
 * Dient der �bersicht
 * @author jcawa
 *
 */
public class Helper {


   
    /**
     * Konvertiert ein beliebiges int 2D Array in einen String zur Ausgabe 
     * @param board
     * @return
     */
    public static String convertIntBoardToString(int[][] board){
		StringBuilder sb = new StringBuilder();
		for(int[] rows : board){
			for(int token : rows){
				sb.append("|");
				sb.append(token);
				
			}
			sb.append("|");
			sb.append("\n");
		}
		
		return sb.toString();
    }

    
    
    public static String convertIntBoardTo1DString(int[][] board){
		StringBuilder sb = new StringBuilder();
		for(int[] rows : board){
			for(int token : rows){		
				sb.append(token);
			}

		}
		
		return sb.toString();
    }
    
    public static String convertIntArrayToString(int[] arr){
    	StringBuilder sb = new StringBuilder();
    	for(int i : arr){
    		sb.append(i);
    		sb.append(" ");
    	}
    	return sb.toString();
    }
    
    public static int[][] deepCopy2DArray(int[][] arr){
    	
    	int[][] copy = new int[arr.length][arr[0].length];
    	for(int i = 0; i < copy.length;i++){
    		copy[i] = Arrays.copyOf(arr[i], arr[i].length);
    	}
    	return copy;
    	

    }
    public static boolean deepEquals2DArray(int[][] arr1, int[][] arr2){
    	boolean result = false;
    	for(int i = 0; i < arr1.length;i++){
    		result = Arrays.deepEquals(arr1, arr2);
    	}
    	
    	return result;
    }
    
    public static int [] ArraytoBinary (int [][] y){
        
        int [] binary = new int [2 * y[0].length];
        
        for (int i = 0; i < y[0].length; i++)
            for (int j = 0; j < y.length; j++){
                if (y [j][i] == 1) {
                    binary [i] += (Math.pow(2, y.length-j-1));
                }   else { if (y [j][i] == -1)
                    binary [i + y[0].length] += (Math.pow(2, y.length-j-1));
                    }
            }
    return binary; 
    }
    
    public static int [][] BinarytoArray (int [] bin){
        //Copy of input
        int [] binary = new int [bin.length];
        for (int i = 0; i < bin.length; i++) {
            binary [i] = bin [i];
        }
        // output Array
        int [][] x = new int [5][binary.length/2];
        
        for (int i = 0; i < (binary.length/2); i++) {
            for (int j = 0; j < x.length; j++) {
                if (binary[i] % 2 == 1)
                    x[x.length-1-j][i] = 1;
                binary[i] /= 2;
            }
            for (int j = 0; j < x.length; j++) {
                if (binary[i + (binary.length/2)] % 2 == 1)
                    x[x.length-1-j][i] = -1;
                binary[i + (binary.length/2)] /= 2;
            }
        }
    return x;
    }

}
