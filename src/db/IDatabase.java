package db;

/**
 * Interface für unsere Datenbank. 
 * Idee ist folgende:
 * Dadurch das nichts von der Implementierung nach außen kommt, können wir die Datenbank intern beliebig verändern,
 * ohne das im restlichen Programm was gemacht werden muss.
 * 
 * Eingefügt in die Datenbank wird:
 * 2D Array mit dem Spielzustand (Board State), einen möglichen Zug (action) und die Bewertung (value)
 * 
 * Board State als 2D Array, intern wird diese entweder so eingespeichert oder binär codiert, können wir dann beliebig verändern
 * 
 * 
 * Datenbank bietet weiterhin die Möglichkeiten:
 * 1. Einen Spielzustand zu finden und einen weiteren Zug hinzuzufügen
 * 2. Einen Spielzustand anzugeben und den höchsten bewerten Zug zurückzubekommen
 * 3. ..
 * 
 * 
 * Intern benutzt die Datenbank erstmal folgendes Schema: Map<int[][],Map<int[],int>> also: Map<state,Map<action,value>>
 * @author jcawa
 *
 */
public interface IDatabase {

}
