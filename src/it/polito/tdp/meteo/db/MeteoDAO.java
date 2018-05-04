package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;


public class MeteoDAO {
	
	
	public MeteoDAO() {
		
	}
	
	public List<Citta> getAllCitta() {
		
		String sql = "SELECT DISTINCT Localita FROM situazione ORDER BY Localita";
		
		List<Citta> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				result.add(new Citta(res.getString("localita")));
			}
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	// ALTRA POSSIBILITA PER L'ESERCIZIO 1
	
	// SELECT localita, AVG(Umidita) FROM situazione WHERE MONTH(Data) = ? GROUP BY localita
	// se vuoi fare questo va bene però devi restiruire comunque un oggetto in java, quindi ad esempio CittaUmidita
	// oppure come nella soluzione dell'anno scorso potresti restituite direttamente l'umidità quindi un Double
	// potresti anche pensare che dato un mese tu restituisca una mappa <nome citta, umidita media> invece 
	// dell'oggetto CittaUmidita
	
	public Double getUmiditaMedia(Month mese, Citta citta) {
		
		String sql = "SELECT AVG(Umidita) AS UM FROM situazione WHERE Localita = ? AND MONTH(Data) = ?";
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setInt(2, mese.getValue());
			
			ResultSet res = st.executeQuery();
			res.next(); // POSIZIONA SULLA PRIMA (E UNICA) RIGA
			Double um = res.getDouble("UM");
			
			conn.close();
			return um;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public List<Rilevamento> getAllRilevamentiCittaMese(Month mese, Citta citta) {
		
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE Localita = ? AND month(Data) = ? ORDER BY Data ASC";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, citta.getNome());
			st.setInt(2, mese.getValue());
		
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				Rilevamento r = new Rilevamento();
				r.setLocalita(rs.getString("Localita"));
				r.setData(rs.getDate("Data").toLocalDate());
				r.setUmidita(rs.getInt("Umidita"));
				rilevamenti.add(r);
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rilevamenti;
		
	}
	
	/*
	 * Metodo di test. Restituisce la lista di tutti i rilevamenti presenti nel DB
	 */
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
}
