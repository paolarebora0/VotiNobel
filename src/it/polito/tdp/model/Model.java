package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {

	private List<Esame> esami; //Letta dal database
	private List<Esame> best; //Soluzione migliore finora
	private double media_best;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	/**
	 * Trova la cobinazione di corsi avete la somma di crediti richiesta che abbia la media dei voti massima
	 * @param numeroCrediti
	 * @return l'elenco dei corsi ottimale, oppure {@code null} se non esiste alcuna
	 * combinazione di corsi che assomma il numero esatto di crediti.
	 */
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//Caso iniziale: avvio della ricursione
		
		best = null;
		media_best = 0.0;
		
		Set<Esame> parziale = new HashSet<>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}
	
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		//Siamo in un caso terminale?
		int crediti = sommaCrediti(parziale);
		
		if(crediti>m) //Fine
			return;
		
		
		if(crediti==m ) { //Valuto la media
			double media = calcolaMedia(parziale);
			
			if(media>media_best) { //MIGLIORE
				media_best = media;
				best = new ArrayList<Esame>(parziale);
				return;
			} else {
				return;
			}
		}
		
		if(crediti<m) {
			//Ho finito gli esami
			if(L==esami.size()) 
				return;
			
		}
		
		//Caso normale: generiamo sotto-problemi
		
		//	L'esame di esami[L] è da aggiungere o no?
		//	---Provo a non aggiungerlo
		cerca(parziale, L+1, m);
		
		//	---Provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		
		parziale.remove(esami.get(L));
	}

	private double calcolaMedia(Set<Esame> parziale) {
		double media = 0.0;
		int crediti = 0;
		for(Esame e: parziale) {
			media += e.getVoto()*e.getCrediti();
			crediti += e.getCrediti();
		}
		return media/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e: parziale)
			somma += e.getCrediti();
		return somma;
	}

}
