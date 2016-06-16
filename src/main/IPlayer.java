package main;

public interface IPlayer {
	
	/**
	 * 
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
