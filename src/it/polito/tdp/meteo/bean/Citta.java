package it.polito.tdp.meteo.bean;

import java.util.ArrayList;
import java.util.List;

public class Citta {

	private String nome;
	private List<Rilevamento> rilevamenti;
	private int counter = 0;
	
	public Citta() {
		
	}
	
	public Citta(String nome) {
		this.nome = nome;
		rilevamenti = new ArrayList<Rilevamento>();
	}
	
	public Citta(String nome, List<Rilevamento> rilevamenti) {
		this.nome = nome;
		this.rilevamenti = rilevamenti;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Rilevamento> getRilevamenti() {
		return rilevamenti;
	}
	
	public Rilevamento getRilevamentoGiorno(int giorno) {
		
		for (Rilevamento r : rilevamenti) {
			if (r.getData().getDayOfMonth() == giorno)
				return r;
		}
		
		return null; // se non trovo nessun rilevamento in quel giorno
		
	}
	

	public Integer getUmiditaGiorno(int giorno) {
		
		return getRilevamentoGiorno(giorno).getUmidita();

	}

	public void setListaRilevamenti(List<Rilevamento> rilevamenti) {
		this.rilevamenti = rilevamenti;
	}
	
	public void setRilevamento(Rilevamento r) {
		rilevamenti.add(r);
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void increaseCounter() {
		this.counter += 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Citta other = (Citta) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
	
}