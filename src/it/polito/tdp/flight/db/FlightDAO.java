package it.polito.tdp.flight.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.AirlineIdMap;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.AirportIdMap;
import it.polito.tdp.flight.model.Route;
import it.polito.tdp.flight.model.RouteIdMap;

public class FlightDAO {

	public List<Airport> getAllAirportsByAirline(AirportIdMap airportIdMap, Airline airline) {
		
		String sql = "SELECT DISTINCT a.* FROM airport AS a , route AS r "
				+ "WHERE (a.Airport_ID = r.Source_airport_ID OR a.Airport_ID = r.Destination_airport_ID) "
				+ "AND r.Airline_ID = ? ORDER BY a.Airport_ID ASC" ;
		
		List<Airport> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, airline.getAirlineId());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Airport a = new Airport(
						res.getInt("Airport_ID"),
						res.getString("name"),
						res.getString("city"),
						res.getString("country"),
						res.getString("IATA_FAA"),
						res.getString("ICAO"),
						res.getDouble("Latitude"),
						res.getDouble("Longitude"),
						res.getFloat("timezone"),
						res.getString("dst"),
						res.getString("tz"));
				list.add(airportIdMap.get(a));
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}
	}
	

	public List<Airline> getAllAirlines(AirlineIdMap airlineIdMap) {
		String sql = "SELECT * FROM airline ORDER BY Airline_ID ASC" ;
		
		List<Airline> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Airline airline = new Airline(res.getInt("Airline_ID"), res.getString("Name"), res.getString("Alias"),
						res.getString("IATA"), res.getString("ICAO"), res.getString("Callsign"),
						res.getString("Country"), res.getString("Active"));
				list.add(airlineIdMap.get(airline));
			}
			
			conn.close();
			
			return list ;
		} catch (SQLException e) {

			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Route> getAllRoutesByAirline(AirportIdMap airportIdMap,Airline airline,RouteIdMap routeIdMap) {
		
		String sql = "Select distinct *  from route as r where r.Airline_ID = ?";
	    List<Route> list = new ArrayList<>() ;
	      
	      try {
	        Connection conn = DBConnect.getConnection() ;

	        PreparedStatement st = conn.prepareStatement(sql) ;
	        st.setInt(1, airline.getAirlineId());
	        ResultSet res = st.executeQuery() ;
	        int routeId=0;
	        while(res.next()) {
	          Airport source = airportIdMap.get(res.getInt("Source_airport_ID"));
	          Airport destination = airportIdMap.get(res.getInt("Destination_airport_ID")); 
	          Route r = new Route(routeId, airline, source, destination);
	          routeId++;
	          list.add(routeIdMap.get(r));
	    
	        }
	        
	        conn.close();
	        
	        return list ;
	      } catch (SQLException e) {

	        e.printStackTrace();
	        return null ;
	      }
	      
	}

	
}
