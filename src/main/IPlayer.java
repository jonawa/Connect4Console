package main;

public interface IPlayer {
	
	/**
	 * Gibt den Zug eines menschlichen oder KI gesteuerten Spielers zurück
	 * in der Range 0 bis COLUMNS -1
	 * @return 
	 */
	public int turn();
	
	
	public void reactToWinOrLose(boolean win);
	
	/**
	 * Gibt zurück ob der Spieler 1 oder 2 ist.
	 * @return
	 */
	public int getPlayerID();
	

}
