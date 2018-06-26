package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {
	
	private FlightDAO dao;
	private AirlineIdMap airlineIdMap;
	private AirportIdMap airportIdMap;
	private RouteIdMap routeIdMap;
	
	private List<Airline> airlines;
	private List<Airport> airports;
	private List<Route> routes;
	
	private SimpleDirectedWeightedGraph<Airport,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new FlightDAO();
		
		this.airlineIdMap = new AirlineIdMap();
		this.airportIdMap = new AirportIdMap();
		this.routeIdMap = new RouteIdMap();
		
		airlines = dao.getAllAirlines(airlineIdMap);
		
		
	}

	public List<Airline> getAirlines() {
		return airlines;
	}
	
	public List<Airport> getAirportsByAirline(Airline airline) {
		return dao.getAllAirportsByAirline(airportIdMap, airline);
	}
	
	public void creaGrafo(Airline airline) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.airports = dao.getAllAirportsByAirline(airportIdMap, airline);
		
		Graphs.addAllVertices(this.grafo, airports);
		
		System.out.println("# vertici: "+ grafo.vertexSet().size());
		
		this.routes = dao.getAllRoutesByAirline(airportIdMap, airline, routeIdMap);
		
		for(Route r : routes) {
			Airport sourceAirport = r.getSourceAirport();
			Airport destinationAirport = r.getDestinationAirport();
			
			if(!sourceAirport.equals(destinationAirport)) {
				
				double weight = LatLngTool.distance(new LatLng(sourceAirport.getLatitude(), sourceAirport.getLongitude()),
						new LatLng(destinationAirport.getLatitude(), destinationAirport.getLongitude()), LengthUnit.KILOMETER);
				Graphs.addEdge(this.grafo, sourceAirport, destinationAirport, weight);
			}
		}
		
		System.out.println("# archi: "+ grafo.edgeSet().size());
	}

	public SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Result> getRaggiungibiliFromAirport(Airport airport) {
		List<Result> raggiunti = new ArrayList<>();
		for(Airport a : grafo.vertexSet()) {
			if(!a.equals(airport)) {
				ConnectivityInspector<Airport,DefaultWeightedEdge> ci = new ConnectivityInspector<Airport,DefaultWeightedEdge>(grafo);
				if(ci.pathExists(airport, a)) {
					DijkstraShortestPath<Airport, DefaultWeightedEdge> dsp = new DijkstraShortestPath<>(grafo, airport, a);
					GraphPath<Airport, DefaultWeightedEdge> p = dsp.getPath();
					if (p != null) {
						raggiunti.add(new Result(a, p.getWeight()));
					}	
				}
			}
		}
		Collections.sort(raggiunti);
		return raggiunti;
		
	}

	public void clear() {
		this.airportIdMap = new AirportIdMap();
		this.routeIdMap = new RouteIdMap();		
	}

	
	
	

	

}
