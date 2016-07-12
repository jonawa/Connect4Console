package db;

public final class TurnWrapper {
	
	private final int[][] state;
	private final int action;
	
	public TurnWrapper(int[][] state, int action) {
		this.state = state;
		this.action = action;
	}
	
	public int[][] getState(){
		return state;
	}
	public int getAction(){
		return action;
	}

}
