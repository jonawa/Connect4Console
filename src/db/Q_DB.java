package db;

import java.util.Map;


import util.Helper;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * Datenbank f�r den Q-Player, benutzt eine HashMap.
 * Key: Spielstand als 2D-Int Array
 * Schl�ssel: Z�ge von diesem Spielzustand und die Bewertung davon 
 *
 */
public class Q_DB {
	
	private static Q_DB testDB2;
	
	
	private  Map<Array2DWrapper, HashMap<Integer, Double>> db;
	
	private Q_DB() {
		db = new HashMap<Array2DWrapper, HashMap<Integer,Double>>();
	}
	
	/**
	 * Singelton Implementaion, hiermit auf die DB zugreifen.
	 * @return
	 */
	public static Q_DB getDB(){
		//TODO HashMap initialisieren.
		if (testDB2 == null)
			testDB2 = new Q_DB();
		return testDB2;
	}
	
	/**
	 * Gibt ein HashMap zur�ck mit Schl�ssel Action, und Wert Value
	 * @param state
	 * @return
	 */
	public HashMap<Integer,Double> get(int[][] state){
		Array2DWrapper stateWrap = new Array2DWrapper(state);
		if (db.containsKey(stateWrap)){
			

			 HashMap<Integer, Double> actionValue = db.get(stateWrap);
			 return actionValue;
		}
		return null;
	}
	

	
	public boolean containsState(int[][] state) {

		Array2DWrapper stateWrap = new Array2DWrapper(state);
		return db.containsKey(stateWrap);
	}
	

	
	/**
	 * Man �bergibt der Mehtode einen State und die Action die ausgef�hrt werden soll,
	 * und es wird der Value zur�ckgegeben, der f�r diese Action vorgesehen ist.
	 * @param state
	 * @param action
	 * @return
	 * @throws Exception wenn State und oder Action nicht vorhanden
	 */

	public double getValueOfStateAndAction(int[][]state, int action){ 

		Array2DWrapper stateWrap = new Array2DWrapper(state);
		
		Double value = db.get(stateWrap).get(action);
		
		if (value != null)
			return value;
		else
			throw new RuntimeException("Fehler sollte nicht passieren. Eintrag in DB nicht vorhanden");
	}
	
	/**
	 * Das schreiben in die Datenbank funktioniert wie folgt:
	 * Als Input bekommt die Datenbank ein 2D Int Array mit dem aktuellen Spielzug(alternativ Bin�rcodierung) ==> State
	 * dann die Reihe in die geworfen werden soll ==> Action und den Wert der diesem Spielzug zugeorndet ist ==> Value
	 * 
	 * Falls kein State vorhanden => komplett neuer Eintrag mit allen Werten
	 * Falls State vorhanden, aber Action noch nicht: Neue Action zu aktuellen State anlegen
	 * 
	 * @param state
	 * @param action
	 * @param value
	 */
	public void put(int[][] state, int action, double value){
		
		Array2DWrapper stateWrap = new Array2DWrapper(state);
		
		
		//Wenn der Zustand/ das aktuelle Board bereits in der DB gespeichert ist:
		if (db.containsKey(stateWrap)){
			
			 HashMap<Integer, Double> key;
			 key = db.get(stateWrap);
			 if (key == null){
				 //Wenn kein Spielzug f�r Boardstate vorhanden, f�ge den aktuellen hinzu:
				System.out.println("Sollte nicht auftreten, wenn Boardstate vorhanden dann ist auch erster Spielzug vorhanden");
			 }
			 else{
				 if (key.containsKey(action)){
					 //do nothing, weil hier ansonsten nur der value ver�ndert werden k�nnte, Zug ist schon vorhanden
					 //und muss der DB nicht mehr hinzugef�gt werden
				 }
				 else {
					 
					 key.put(action, value);
				 }
			 }
		}
		
		//Wenn der aktuelle Zustand noch nicht in der DB gespeichert ist.
		else{
			//neue HashMap erzeugen:
			HashMap<Integer, Double> key = new HashMap<Integer,Double>();
			key.put(action, value);
			db.put(stateWrap, key);			
		}		
	}



	/**
	 * Speichert die Datenbank, filename ist Pfad und Dateiname zusammen, als Endung am besten .ser eingeben
	 */
	public void saveDB(String filename){

		
		try{
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(db);
			out.close();
			fileOut.close();
			System.out.println("Die Datenbank wurde serialisiert");
		}
		catch(IOException i){
			System.out.println("Fehler beim Speichern der Datenbank!");
			i.printStackTrace();
		}
			
	}
	/**
	 * l�dt die Datenbank und weist der Q_DB die gelandene Datenbank zu
	 * @param filename
	 */
	public void loadDB(String filename){
		

		Map<Array2DWrapper, HashMap<Integer, Double>> dbLoaded = null;
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream (filename);
			ObjectInputStream in = new ObjectInputStream (fileIn);
			dbLoaded = (Map<Array2DWrapper, HashMap<Integer, Double>>) in.readObject();
			db = dbLoaded;
			in.close();
			fileIn.close();
			System.out.println("Die Datenbank wurde erfolgreich geladen");
			}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

	}
	

	/**
	 * Die Datenbank nach dem State und der Action die bewertet werden soll und addiert den Wert von addValue
	 * zu dem aktuellen Wert(value).
	 * 
	 * @param state aktuelle Spielposition als 2D Int Array
	 * @param action Action(also Reihe, in die geschmissen wurde) die bewertet werden soll
	 * @param Bewertung als Double, �berschreibt den alten Value der in der DB steht
	 * @return true, wenn das Update erfolgreich war, wirft zum Testen einen Fehler wenn das Update nicht erfolgreich war
	 */
	public boolean update(int[][] state, int action, double newValue){

		
		Array2DWrapper stateWrap = new Array2DWrapper(state);

		
		if(db.containsKey(stateWrap) == false)
			throw new RuntimeException("Datenbank soll geupdatet werden enth�hlt aber das Element nicht");
		
		// newValue wird vom Q-Player selbst wie folgt berechnet:
		// newValue (1-alpha)*previousValue+alpha*addValue)
		// und der Datenbank hiermmit �bergeben 
		db.get(stateWrap).put(action, newValue);

		
		return true;
	}
	
	/**
	 * wird aktuell nicht mehr benutzt, da das schreiben in eine Txt Datei zu ineffizient ist
	 */
	public void saveDBToTxt(){
		System.out.println("Datenbank Elemente" + db.size());
		 FileWriter fw;
		try {
			fw = new FileWriter("db.txt");
		    BufferedWriter bw = new BufferedWriter(fw);
		    
		    for(Array2DWrapper stateWrap : db.keySet()){
		    	
		    	HashMap<Integer, Double> valueActionMap = db.get(stateWrap);
		    	
		    	for(Integer action : valueActionMap.keySet()){

		    		bw.write(Helper.convertIntBoardToString(stateWrap.getArr()));
		    		//System.out.println(Helper.convertIntBoardToString(state));
		    		bw.write("\t");
		    		bw.write(action.toString());
		    		//System.out.println(action.toString());
		    		bw.write("\t");
		    		bw.write(valueActionMap.get(action).toString());
		    		//System.out.println(valueActionMap.get(action));
		    		bw.newLine();
					
		    	
		    	}
			  
		    	bw.newLine();bw.newLine();
		    	//System.out.println("\n \n");
		    }


		    bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    public static void main(String[] args) {
		System.out.println("Teste Array List");
		int[][] arr1 = {{1,2,3},{4,5,6}};
		int[][] arr2 = {{1,2,3},{4,5,6}};
		
		List<int[]> list = Arrays.asList(arr1);
		List<int[]> list2 = Arrays.asList(arr2);
		
		System.out.println(list.equals(list2));
		
		
		int[] arr3 = {1,2,3,4,5};
		int[] arr4 = {1,2,3,4,5};
		List<int[]> list3 =Collections.unmodifiableList( Arrays.asList(arr3));
		List<int[]> list4 = Arrays.asList(arr4);
		System.out.println(list3.equals(list4));
		
	}
    
    public int getSize(){
    	return db.size();
    }


}


