package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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
		int topRow=Game.ROWS-1;
		
		if (Game.BOARDISEMPTY){
			if (debug){
				System.out.println("Zuerst in die Mitte");
			}
			return Game.COLUMNS/2+1;
		}
		
		//Kann ich in einem diesem Zug gewinnen?
		for (column=0; column<=Game.COLUMNS-1; column++){
			row=placeVirtualDisk(column);
			if (row!=1){
				Game.board[row][column]=playerID;
				if (Game.checkWin(playerID, win, row, column)){
					Game.board[row][column]=0;
					return column;
				}
				else{
					Game.board[row][column]=0;
				}
			}
		}
		
		//Kann der Gegner im nächsten zu gewinnen?
		for (column=0; column<=Game.COLUMNS-1; column++){
			row=placeVirtualDisk(column);
			if (row!=1){
				Game.board[row][column]=enemyID;
				if (Game.checkWin(enemyID, win, row, column)){
					Game.board[row][column]=0;
					return column;
				}
				else{
					Game.board[row][column]=0;
				}
			}
		}
		
		//Defensiv Spielen:
		for (column=0; column<=Game.COLUMNS-1; column++){
			row=placeVirtualDisk(column);
			for (int i=win-1; i>1; i--){
				if(row != 1){
					Game.board[row][column]=enemyID;
					if (Game.checkWin(enemyID, i, row, column)){
						possibleSolutions.add(column);
					}
					Game.board[row][column]=0;
				}
			}
		}
		
		//Gibt es eine Möglichkeit meinem Gegener nicht zu helfen in der nächsten Runde zu gewinnen?
		Iterator<Integer> posSolu = possibleSolutions.iterator();
		int possibleColumn;
		while (posSolu.hasNext()){
			possibleColumn=posSolu.next();
			int nextRow=placeVirtualDisk(possibleColumn)+1;
			if (nextRow<=topRow){  
				Game.board[nextRow][possibleColumn]=enemyID;
				if (Game.checkWin(enemyID, win, nextRow, possibleColumn)){
					posSolu.remove();
				}
				Game.board[nextRow][possibleColumn]=0;
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
		     return (int)possibleSolutions.get(0);
		}
		
		//Füge verbotene Züge zu veryBadIdeas hinzu.
		 for (int col = 0; col < 7; col++) {
		      if (Game.board[col][topRow] != 0)
		        veryBadIdeas.add(col);
		      else
		      { // füge züge hinzu, de
		        int nextRow = placeVirtualDisk(col) + 1;
		        if (nextRow <= topRow ){
		        	Game.board[nextRow][possibleColumn]=enemyID;
		        	if (Game.checkWin(enemyID, win, nextRow, possibleColumn)){
		        		veryBadIdeas.add(col);
		        	}
		        	Game.board[nextRow][possibleColumn]=0;
		        }
		      }
		 }
		
	}

	public static int placeVirtualDisk(int column){
		//Beginne in der untersten Reihe
		int row = Game.ROWS -1;
		
		//Bis zur obersten Reihe:
		for(int i = row; i>=0; i-- ){
			//Sobald ein Feld leer ist, gibt die Zeile dieses Felds zurück
			if(Game.board[i][column] == 0){return i;}				
		}
		return -1;
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
