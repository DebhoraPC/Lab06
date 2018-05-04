package it.polito.tdp.meteo;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.CittaUmidita;
import it.polito.tdp.meteo.db.MeteoDAO;


public class Model {
	

	private final static int COST = 100;
	//private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO dao;
	
	private List<Citta> leCitta;
	
	private List<Citta> best;

	public Model() {
		
		dao = new MeteoDAO();
		leCitta = dao.getAllCitta();
		
	}
	
	// ABBIAMO USATO QUESTA MODALITA' PER L'ESERCIZIO 1
	public Double getUmiditaMedia(Month mese, Citta citta) {
	
		return dao.getUmiditaMedia(mese, citta);
		
	}
	
	// ALTRA POSSIBILE MODALITA' PER L'ESERCIZIO 1
	public List<CittaUmidita> getUmiditaMedia(Month mese) {
		return null;
	} 
	
	public List<Citta> getLeCitta() {
		return leCitta;
	}
	
	public List<Citta> calcolaSequenza(Month mese) {
		
		List<Citta> parziale = new ArrayList<>();
		best = null;
		
		for (Citta citta : leCitta) {
			citta.setListaRilevamenti(dao.getAllRilevamentiCittaMese(mese, citta));
			parziale.add(citta);
		}
		
		cerca(parziale, 0);
		
		return best;
		
	}
	
	private void cerca(List<Citta> parziale, int livello) {
		
		// CASO TERMINALE
		if (livello == NUMERO_GIORNI_TOTALI) {
			Double costo = calcolaCosto(parziale);
			if (best == null || costo < calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
			
			//System.out.println(parziale); // Debug
		} 
		// CASO INTERMEDIO: ANDARE AVANTI TROVANDO NUOVE SOLUZIONI
		else {
			for (Citta prova : leCitta) {
				if (aggiuntaValida(prova, parziale)) {
					// allora la provo ad aggiungerla
					parziale.add(prova);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1); //potrei anche fare parziale.remove(prova)
				}
			}
		}
		
	}

	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		// VERIFICA GIORNI MASSIMI
		
		int conta = 0;
		
		for (Citta precedente: parziale) 
			if (precedente.equals(prova))
				conta++;

		if (conta >= NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		// VERIFICA GIORNI MINIMI
		
		if (parziale.size() == 0) // primo giorno
			return true;
		
		if (parziale.size() == 1 || parziale.size() == 2) // secondo o terzo giorno: non posso cambiare
			return parziale.get(parziale.size()-1).equals(prova);
		
		if (parziale.get(parziale.size()-1).equals(prova)) // giorni successivi: posso sempre rimanere
			return true;
		
		// sto cambiando citta
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) &&
				parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
		
		return false;
		
	}
	
	private Double calcolaCosto(List<Citta> parziale) {
		
		// sommatoria delle umidita in ciascuna citta considerando il rilevamento del giorno giusto
		// SOMMA parziale.get(giorno-1).getRilevamenti().get(giorno-1)
		// a cui sommo 100 * numero di volte in cui cambio città
		
		Double costo = 0.0; int cambio = 0;
		
		for (int giorno = 0; giorno < parziale.size(); giorno++) {
			
			Citta citta = parziale.get(giorno);
			
			if (citta.getRilevamentoGiorno(giorno) != null) 
				costo += citta.getUmiditaGiorno(giorno);
			
			if (giorno != parziale.size()-1) {
				if (!citta.equals(parziale.get(giorno+1)))
					cambio++;
			}
		}
		
		// adesso devo sommare 100 ogni volta che cambio città
		return costo*(COST*cambio);
		
	}
	
}
