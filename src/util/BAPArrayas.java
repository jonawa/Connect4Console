package util;

public class BAPArrayas {
	public static void main(String[] args) {
        
        int [][] y = {{0,0,1,1},{1,1,-1,1},{-1,1,-1,1},{1,-1,1,-1},{1,-1,1,-1}};
        int [][] x;
        int [] binar;
        
        
        System.out.print("y = \n");
        ShowArray (y);
        
        binar = ArraytoBinary (y);
        System.out.print("\nBinary = \n");
        ShowArray(binar);
        
        x = BinarytoArray (binar);
        System.out.print("\nx = \n");
        ShowArray (x);
        
        
        
    }
    
    public static int [] ArraytoBinary (int [][] y){
        
        int [] binary = new int [2 * y[0].length];
        
        for (int i = 0; i < y[0].length; i++)
            for (int j = 0; j < y.length; j++){
                if (y [j][i] == 1) {
                    binary [i] += (Math.pow(2, y.length-j-1));
                }   else { if (y [j][i] == -1)
                    binary [i + y[0].length] += (Math.pow(2, y.length-j-1));
                    }
            }
    return binary; 
    }
    
    public static int [][] BinarytoArray (int [] bin){
        //Copy of input
        int [] binary = new int [bin.length];
        for (int i = 0; i < bin.length; i++) {
            binary [i] = bin [i];
        }
        // output Array
        int [][] x = new int [5][binary.length/2];
        
        for (int i = 0; i < (binary.length/2); i++) {
            for (int j = 0; j < x.length; j++) {
                if (binary[i] % 2 == 1)
                    x[x.length-1-j][i] = 1;
                binary[i] /= 2;
            }
            for (int j = 0; j < x.length; j++) {
                if (binary[i + (binary.length/2)] % 2 == 1)
                    x[x.length-1-j][i] = -1;
                binary[i + (binary.length/2)] /= 2;
            }
        }
    return x;
    }
    
    public static void ShowArray ( int [] x){
        
        for (int i = 0; i < x.length; i++)
            System.out.print("[" + x[i] + "]");
        System.out.println("");
    }
    public static void ShowArray ( int [][] x){
        
        for (int i = 0; i < x.length; i++){
            for (int j = 0; j < x[0].length; j++)
                System.out.print("[" + x[i][j] + "]");
            System.out.println("");
        }        
    }
}
