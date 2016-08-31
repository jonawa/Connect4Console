package db;


import java.io.Serializable;


import util.Helper;

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
		return java.util.Arrays.deepHashCode( arr );
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

	
	public static void main(String[] args) {
		//Testen ob Equals Methode funktioniert
		
		int[][] arr1 = {{1,2,3},{4,5,6}};
		int[][] arr2 = {{1,2,3},{4,5,6}};
		Array2DWrapper a1 = new Array2DWrapper(arr1);
		Array2DWrapper a2 = new Array2DWrapper(arr2);
		
		System.out.println("Hashcode von alt " + a1.hashCode());
		System.out.println("Hascode von neu " + a2.hashCode());
		
//		System.out.println(a1.equals(a2));
//		System.out.println(a2.equals(a1));
		
		TestDB2 db = TestDB2.getDB();
		
		db.put(arr1, 1, 50);
		db.update(arr1, 1, 333);
		
		db.saveDBToTxt();
		
		
		
	}

	

}
