package db;

import java.util.Map;

import util.Helper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	 * Methode wird dazu benutzt, am Ende eines Spiels: alle Actions, in den jeweiligen States ab bzw. aufzuwerten.
	 * Dabei sucht die Datenbank nach dem State und der Action die bewertet werden soll und addiert den Wert von addValue
	 * zu dem aktuellen Wert(value).
	 * 
	 * @param state aktuelle Spielposition als 2D Int Array
	 * @param action Action(also Reihe, in die geschmissen wurde) die bewertet werden soll
	 * @param addValue Bewertung als int, von dem was hinzugefügt werden soll zum aktuellen Value
	 * @return true, wenn das Update erfolgreich war, false wenn das Update nicht erfolgreich war
	 */
	public boolean update(int[][] state, int action, int addValue){
		
		//TODO Implementieren
		return false;
	}
}


