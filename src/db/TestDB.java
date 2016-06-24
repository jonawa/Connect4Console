package db;

import java.util.Map;

import util.Helper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * 
 * @author jcawa
 *
 */
public class TestDB {
	
	private static TestDB testDB;
	
	
	private  Map<int[][], HashMap<Integer, Integer>> db;
	
	private TestDB() {
		db = new HashMap<int[][], HashMap<Integer,Integer>>();
	}
	
	/**
	 * Singelton Implementaion, hiermit auf die DB zugreifen.
	 * @return
	 */
	public static TestDB getDB(){
		if (testDB == null)
			testDB = new TestDB();
		return testDB;
	}
	
	/**
	 * Gibt ein HashMap zurück mit Schlüssel Action, und Wert Value
	 * @param state
	 * @return
	 */
	public HashMap<Integer,Integer> get(int[][] state){
		if (db.containsKey(state)){
			
			 HashMap<Integer, Integer> actionValue;
			 actionValue = db.get(state);
			 return actionValue;
		}
		return null;
	}
	

	
	public boolean containsState(int[][] state) {
		
		return db.containsKey(state);
	}
	

	
	/**
	 * Man übergibt der Mehtode einen State und die Action die ausgeführt werden soll,
	 * und es wird der Value zurückgegeben, der für diese Action vorgesehen ist.
	 * @param state
	 * @param action
	 * @return
	 * @throws Exception wenn State und oder Action nicht vorhanden
	 */
	public int getValueOfStateAndAction(int[][]state, int action){
		
		Integer value = db.get(state).get(action);
		
		if (value != null)
			return value;
		else
			throw new RuntimeException("Fehler sollte nicht passieren. Eintrag in DB nicht vorhanden");

		
	}
	
	/**
	 * Das schreiben in die Datenbank funktioniert wie folgt:
	 * Als Input bekommt die Datenbank ein 2D Int Array mit dem aktuellen Spielzug(alternativ Binärcodierung) ==> State
	 * dann die Reihe in die geworfen werden soll ==> Action und den Wert der diesem Spielzug zugeorndet ist ==> Value
	 * 
	 * Falls kein State vorhanden => komplett neuer Eintrag mit allen Werten
	 * Falls State vorhanden, aber Action noch nicht: Neue Action zu aktuellen State anlegen
	 * 
	 * @param state
	 * @param action
	 * @param value
	 */
	public void put(int[][] state, int action, int value){
		//TODO Das kann doch eigentlich nicht effizient sein oder?
		
		
		
		
		//Wenn der Zustand/ das aktuelle Board bereits in der DB gespeichert ist:
		if (db.containsKey(state)){
			
			 HashMap<Integer, Integer> key;
			 key = db.get(state);
			 if (key == null){
				 //Wenn kein Spielzug für Boardstate vorhanden, füge den aktuellen hinzu:
				System.out.println("Sollte nicht auftreten, wenn Boardstate vorhanden dann ist auch erster Spielzug vorhanden");
			 }
			 else{
				 if (key.containsKey(action)){
					 //do nothing, weil hier ansonsten nur der value verändert werden könnte, Zug ist schon vorhanden
					 //und muss der DB nicht mehr hinzugefügt werden
				 }
				 else {
					 key.put(action, value);
				 }
			 }
		}
		
		//Wenn der aktuelle Zustand noch nicht in der DB gespeichert ist.
		else{
			//neue HashMap erzeugen:
			//TODO könnte man mit max mögliche Züge initialisieren
			HashMap<Integer, Integer> key = new HashMap<Integer,Integer>();
			key.put(action, value);
			db.put(state, key);
			
			
			
		}
			
		
	}

	/**
	 * is currently used to either print out all data points or just the database size
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
//		for(int[][] key : db.keySet()){
//
//			
//			HashMap<Integer, Integer> value = db.get(key);
//			
//			
//			for(Integer intKey: value.keySet()){
//				sb.append(key);
//				/* sb.append(Helper.convertIntBoardTo1DString(key)); */
//				sb.append("\t");
//				
//				sb.append(intKey);
//				sb.append("\t");
//				
//				sb.append("value"); //bisher noch nicht drin
//				sb.append("\n");
//				
//
//				
//			}
//			
//			
//		}
		
		sb.append("Die Menge der Keys in der DB: " + db.size());
		return sb.toString();
	}

	/**
	 * Achtung noch nicht vollständig und ungetestet
	 */
	public void saveDB(){
		try {
			FileOutputStream fileOut =
			         new FileOutputStream("");
			         ObjectOutputStream out = new ObjectOutputStream(fileOut);
			         try {
						out.writeObject(db);
						out.close();
				         fileOut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			         
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Der Methode wird ein 2D Int Array übergeben. Die Datenbank überprüft, ob dieser State vorhanden ist.
	 * Falls nicht wird -1 zurückgeben, falls vorhanden wird die Action aus der Datenbank gesucht, die den höchsten Value hat.
	 * Die Action ist ein Wert von 0 bis zur Größe des aktuellen Spielfeldes -1.
	 * Also die Spalte mit dem höchsten Wert
	 * 
	 * @param state aktuelle Spielposition als 2D Int Array
	 * @return int, gibt die Reihe(action) zurück in die geworfen werden soll
	 * 
	 */
	public int maxValueActionForState(int[][] state){
		//TODO Implementieren
		return -1;
	}
	/**
	 * Die Datenbank nach dem State und der Action die bewertet werden soll und addiert den Wert von addValue
	 * zu dem aktuellen Wert(value).
	 * 
	 * @param state aktuelle Spielposition als 2D Int Array
	 * @param action Action(also Reihe, in die geschmissen wurde) die bewertet werden soll
	 * @param addValue Bewertung als int, von dem was hinzugefügt werden soll zum aktuellen Value
	 * @return true, wenn das Update erfolgreich war, false wenn das Update nicht erfolgreich war
	 */
	public boolean update(int[][] state, int action, int addValue){
		if(db.containsKey(state) == false)
			throw new RuntimeException("Datenbank soll geupdatet werden enthählt aber das Element nicht");
		
		
		int previousValue = db.get(state).get(action); 
		//put von HashMap überschreibt einfach das Mapping des Keys auf das bisherige Value, siehe Java Doc.
		db.get(state).put(action, previousValue + addValue);
		return true;
	}
	
	public void saveDBToTxt(){
		System.out.println("Datenbank Elemente" + db.size());
		 FileWriter fw;
		try {
			fw = new FileWriter("db.txt");
		    BufferedWriter bw = new BufferedWriter(fw);
		    
		    for(int[][] state : db.keySet()){
		    	
		    	HashMap<Integer, Integer> valueActionMap = db.get(state);
		    	
		    	for(Integer action : valueActionMap.keySet()){
		    		//TODO Extra Methode schreiben, damit state nicht in einer Zeile ausgegeben wird
		    		//Das Problem ist hier das \n in der Helper Methode, wird nicht von Buffered Writer erkannt..
		    		bw.write(Helper.convertIntBoardToString(state));
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




}


