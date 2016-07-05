package db;


import java.util.Arrays;



public final class ArrayWrapper {
	
	private final int[] arr;
	
	public ArrayWrapper(final int [] arr) {
		this.arr = arr;
	}

	public final int[] getArr() {
		return arr;
	}


	@Override
	public int hashCode() {
		return java.util.Arrays.hashCode(arr);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayWrapper other = (ArrayWrapper) obj;
		if (Arrays.equals(arr, other.getArr()))
			return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println("Testen des Array Wrappers");
		
		int[] arr1 = {1,2,3,4,5};
		int[] arr2 = {1,2,3,4,5};
		ArrayWrapper aw1 = new ArrayWrapper(arr1);
		ArrayWrapper aw2 = new ArrayWrapper(arr2);
		
		
		System.out.println(aw1.hashCode());
		System.out.println(aw2.hashCode());
		
		System.out.println("Beides die gleichen? " + aw1.equals(aw2));
	}

}