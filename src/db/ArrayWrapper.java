package db;

import java.util.Arrays;

import util.Helper;

public final class  ArrayWrapper {
	
	private final int[] arr;
	
	public ArrayWrapper(int [] arr) {
		this.arr = arr;
	}

	public int[] getArr() {
		return arr;
	}


	@Override
	public int hashCode() {
//		System.out.println("Hashcode called");
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + Arrays.hashCode(arr);
//		return result;
		return Arrays.hashCode(arr);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){

			return false;
			
		}
		ArrayWrapper other = (ArrayWrapper) obj;

		return Arrays.equals(arr, other.getArr());
	}
	
	public static void main(String[] args) {
		
	}
}