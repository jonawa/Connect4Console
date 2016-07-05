package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class NormalKI implements IPlayer{
	
	private int playerID;
	private int enemyID;
	private boolean debug = true;
	
	/**
	 * Konstruktor für die "normale" KI, muss 1 oder 2 zugewiesen weden.
	 * @param playerID
	 */
	public NormalKI (int playerID){
		this.playerID=playerID;
		if (playerID==1){
			enemyID=2;}
		else{
			enemyID=1;	
		}
	}
	
	@Override
	public int turn() {
		if (debug){
			System.out.println("Normale KI am Zug");
		}
		
		ArrayList<Integer> possibleSolutions = new ArrayList<Integer>();
		ArrayList<Integer> veryBadIdeas= new ArrayList<Integer>();
		int column;
		int row;
		int win= Game.WINCOUNT;
		// int topRow= Game.ROWS-1;
		int topRow = 0;
		
		if (Game.BOARDISEMPTY){
			if (debug){
				System.out.println("Zuerst in die Mitte");
			}
			return Game.COLUMNS/2;
		}
		
		//Kann ich in einem diesem Zug gewinnen?
		for (column=0; column<=Game.COLUMNS-1; column++){
			row= placeVirtualDisk(column);
			if ((row!=-1) && checkVirtualWin(playerID, win, row, column)){
				if (debug){
					System.out.println("Ich kann gewinnen! Wähle: "+(column+1));
				}
				return column;
			}
		}
		
		//Kann der Gegner im nächsten zu gewinnen?
		for (column=0; column<=Game.COLUMNS-1; column++){
			row=placeVirtualDisk(column);
			
			if ((row!=-1) && checkVirtualWin(enemyID, win, row, column)){					
					if (debug){
						System.out.println("Ich muss verhindern, dass mein Gegner gewinnt: Wähle " + (column+1));
					}
					return column;
			}
		}
		
		//Defensiv Spielen:
		for (column=0; column<=Game.COLUMNS-1; column++){
			row=placeVirtualDisk(column);
			for (int i=win-1; i>1; i--){
				if ((row!=-1) && checkVirtualWin(enemyID, i, row, column)){
						possibleSolutions.add(column);
						if (debug){
							System.out.println("Habe vielleicht etwas nützliches gefunden: "+ (column+1));
						}
				}
			}
		}
		
		//Gibt es eine Möglichkeit meinem Gegener nicht zu helfen in der nächsten Runde zu gewinnen?
		Iterator<Integer> posSolu = possibleSolutions.iterator();
		int possibleColumn;
		
		while (posSolu.hasNext()){
			possibleColumn=posSolu.next();
			int nextRow=placeVirtualDisk(possibleColumn) -1;
			if ((nextRow>=topRow) && (checkVirtualWin(enemyID, Game.WINCOUNT, nextRow, possibleColumn))){  
				posSolu.remove();
				if (debug){
					System.out.println("Entferne Möglichkeit, es beleibt:" + possibleSolutions);
				}
			}
		}
		
		//Bevorzuge Lösungen in der Mitte des Felds.
		int nrOfSolutions = possibleSolutions.size();
		for (int i=0; i<nrOfSolutions; i++){
			if (possibleSolutions.get(i)>1 && possibleSolutions.get(i)<Game.COLUMNS-2){
				possibleSolutions.add(possibleSolutions.get(i));
			}
		}
		//Falls noch eine Möglichkeit vorhanden: wähle zufällig
		if (!possibleSolutions.isEmpty()){
			Collections.shuffle(possibleSolutions);
			if (debug){
		          System.out.println("Wähle zufällig aus allem was nicht so schlehct erscheint");
			}
		    return (int)possibleSolutions.get(0);
		}
		
		//Füge verbotene Züge zu veryBadIdeas hinzu.
		 for (int col = 0; col <= Game.COLUMNS-1; col++) {
		      if (Game.getBoard()[topRow][col] != 0){
		    	  veryBadIdeas.add(col);
		      }  
		      else
		      { // füge Züge hinzu, die es meinem Gegener ermöglichen zu gewinnen
		        int nextRow = placeVirtualDisk(col) - 1;
		        if ((nextRow >= topRow )&&(checkVirtualWin(enemyID, win, nextRow, col))){
		        	veryBadIdeas.add(col);
		        }
		      }
		 }
		 
		 Random grn = new Random();
		 //alle Möglichkeiten doof?
		 if (veryBadIdeas.size()==Game.COLUMNS){
			 do{
				 column = grn.nextInt(Game.COLUMNS/2+1)+grn.nextInt(Game.COLUMNS/2+1);
			 }
			 while (Game.getBoard()[topRow][column]!=0);
			 if (debug){
					System.out.println("Das ist alles nicht gut. Suche eine Spalte, die nciht voll ist.");
				}
			 return column;
		 }
		 else{
			 if (debug){
					System.out.println("Suche eine Spalte die okay ist.");
				}
			 do{
				 column = grn.nextInt(Game.COLUMNS/2+1)+grn.nextInt(Game.COLUMNS/2+1);
			 }
			 while (veryBadIdeas.contains(column));
		 }
		 if (debug){
	          System.out.println("Das sollte funktionieren:"
	            + (column+1));
		 }
		 return column;
		
	}

	public static int placeVirtualDisk(int column){
		//Beginne in der untersten Reihe
		int row = Game.ROWS -1;
		
		//Bis zur obersten Reihe:
		for(int i = row; i>=0; i-- ){
			//Sobald ein Feld leer ist, gibt die Zeile dieses Felds zurück
			if(Game.getBoard()[i][column] == 0){return i;}				
		}
		return -1;
	}
	
public static boolean checkVirtualWin(int player, int win, int row, int column){
		
		//Methoden sollen das für den statischen WINCOUNT prüfen, nicht für 4
	
		if(checkWinRow( player,  win, row,  column))
			return true;
		if(checkWinColumn( player,  win, row,  column))
			return true;
		if(checkWinDiagonal1( player,  win, row,  column))
			return true;
		if(checkWinDiagonal2( player,  win, row,  column))
			return true;
		
		return false;
	}
	
	private static boolean checkWinDiagonal2(int player, int win, int row, int column) {
		//rechts nach links
			int tokensfound=1;
				
			int j= row-1;
			//Schaue rechts:
			for (int i=column+1; i<=Game.COLUMNS-1; i++){
				if (j>=0 && Game.getBoard()[j][i]==player){
					tokensfound++;
					j--;
				}
					else {break;}
			}
			j= row+1;
			//Schaue links:
			for (int i=column-1; i>=0; i--){
				if (j<=Game.ROWS-1 && Game.getBoard()[j][i]==player){
					tokensfound++;
					j++;
				}
				else {break;}
			}
			if (tokensfound>=win){
				return true;
			}
			return false;
	}
	
	private static boolean checkWinDiagonal1(int player, int win, int row, int column) {
		// links nach rechts
		int tokensfound=1;
		
		int j= row+1;
		//Schaue rechts:
		for (int i=column+1; i<=Game.COLUMNS-1; i++){
			if (j<=Game.ROWS-1 && Game.getBoard()[j][i]==player){
				tokensfound++;
				j++;
			}
			else {break;}
		}
		j= row-1;
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			if (j>=0 && Game.getBoard()[j][i]==player){
				tokensfound++;
				j--;
			}
			else {break;}
		}
		if (tokensfound>=win){
			return true;
		}
		return false;
	}

	private static boolean checkWinColumn(int player, int win, int row, int column) {
		int tokensfound=1;
		
		
		// Schaue in der Spalte nach unten:
		for (int i=row+1; i<=Game.ROWS-1; i++){
			if (Game.getBoard()[i][column]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		if (tokensfound>=win){
			return true;
		}
		return false;
		
	}

	private static boolean checkWinRow(int player, int win, int row, int column) {
		
		int tokensfound=1;
		
		
		//Schaue rechts:
		for (int i=column+1; i<=Game.COLUMNS-1; i++){
			if (Game.getBoard()[row][i]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		//Schaue links:
		for (int i=column-1; i>=0; i--){
			if (Game.getBoard()[row][i]==player){
				tokensfound++;
			}
			else {
				break;
			}
		}
		if (tokensfound>=win){
			return true;
		}
		
		return false;
	}

	
	@Override
	public void reactToWinOrLose(boolean win) {
		// bleibt leer		
	}

	@Override
	public int getPlayerID() {
		return playerID;
	}
	
	

}
