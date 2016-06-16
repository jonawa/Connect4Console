package util;


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

}
