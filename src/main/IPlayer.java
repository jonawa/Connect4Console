package main;

public interface IPlayer {
	
	/**
	 * Gibt den Zug eines menschlichen oder KI gesteuerten Spielers zur�ck
	 * in der Range 0 bis COLUMNS -1
	 * @return 
	 */
	public int turn();
	
	
	public void reactToWinOrLose(boolean win);
	
	/**
	 * Gibt zur�ck ob der Spieler 1 oder 2 ist.
	 * @return
	 */
	public int getPlayerID();
	

}
