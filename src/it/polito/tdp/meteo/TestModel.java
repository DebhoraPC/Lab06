package it.polito.tdp.meteo;

import java.time.Month;

import it.polito.tdp.meteo.bean.Citta;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println("Valori rilevati per il mese di " + Month.of(1).toString() + ":\n");
		for (Citta c : m.getLeCitta()) {
			Double u = m.getUmiditaMedia(Month.of(1), c);
			System.out.println(String.format("Citta %s: umidita %.2f%%\n", c.getNome(), u));
		}
		System.out.println(m.calcolaSequenza(Month.of(5)));

		System.out.println("\n\n");
		
		System.out.println("Valori rilevati per il mese di " + Month.of(12).toString() + ":\n");
		for (Citta c : m.getLeCitta()) {
			Double u = m.getUmiditaMedia(Month.of(12), c);
			System.out.println(String.format("Citta %s: umidita %.2f%%\n", c.getNome(), u));
		}
		System.out.println(m.calcolaSequenza(Month.of(12)));
		
	}

}
