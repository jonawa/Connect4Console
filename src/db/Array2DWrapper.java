package db;


import java.io.Serializable;


import util.Helper;

/**
 * Wird verwendet, da man nicht einfach ein IntArray als Schlüssel in einer HashMap verwenden kann
 * Es würde ansonsten Probleme geben das ein normales Int Array keine hash und keine equals Funktion hat
 * 
 *
 */
public final class Array2DWrapper implements Serializable{
	
	private final int[][] arr;
	
	public Array2DWrapper(final int [][] arr) {
		this.arr = arr;
	}

	public final int[][] getArr() {
		return arr;
	}


	@Override
	public int hashCode() {
		return java.util.Arrays.deepHashCode(arr);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Array2DWrapper other = (Array2DWrapper) obj;
		if (Helper.deepEquals2DArray(arr, other.getArr()))
			return true;
		return false;
	}



	

}
