package util;

import java.util.Arrays;

/**
 * Helper Klasse in dem wir alles auslagern können, was wir nicht in die Main packen wollen, aber keine eigene Klasse rechtfertigt.
 * Dient der Übersicht
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
    	
//    	boolean[][] nv = new boolean[foo.length][foo[0].length];
//    	for (int i = 0; i < nv.length; i++)
//    	     nv[i] = Arrays.copyOf(foo[i], foo[i].length);
    }

}
