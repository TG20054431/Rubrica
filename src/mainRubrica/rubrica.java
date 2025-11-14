package mainRubrica;

import java.util.ArrayList;

public class rubrica {

	private static ArrayList<String> rubrica;
	public static final int MAX_DIM = 6;
	
	//-- creazione rubrica
	public static void creaRubrica() {
		
		rubrica = new ArrayList<String>();
	}
	
	//-- calcolo numero di elementi
	public static int numElem() {
		
		return rubrica.size();
	}
	

	//-- cancellazione rubrica
	public static void svuotaRubrica() {
		
		rubrica.clear();
	}
	
	//-- aggiungi contatto
	public static int aggiungiContatto(String contatto) {
		
		if(rubrica.size() >= 6) {
			
			return -1;
		}
		
		if(rubrica.contains(contatto)) {
			
			return 0;
		}
		
		rubrica.add(contatto);
		return 1;
		
	}
	
	// --ricerca contatto
	public static ArrayList<String> ricercaContatto(String prefisso){
		
		ArrayList<String> ris = new ArrayList<String>();
		
		for(String next: rubrica) {
			
			if(next.startsWith(prefisso)) {
				
				ris.add(next);
			}
		}
		return ris;
	}
	
	// --cancella contatto
	public static boolean cancellaContatto(String prefisso) {
		
		return rubrica.removeAll(ricercaContatto(prefisso));
	}
}
