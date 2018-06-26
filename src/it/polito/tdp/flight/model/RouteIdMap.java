package it.polito.tdp.flight.model;

import java.util.HashMap;
import java.util.Map;

public class RouteIdMap {
	
	private Map<Integer, Route> map;

	public RouteIdMap() {
		this.map = new HashMap<>();
	}
	
	public Route get(Route route) {
		Route old = map.get(route.getRouteId());
		if(old==null) {
			//nella mappa non c'è questo corso => LO AGGIUNGO
			map.put(route.getRouteId(), route);
			return route;
		}
		
		//avevo già inserito quell'oggetto
		return old;
	
	}
	
	public Route get(int routeId) {
		return map.get(routeId);
	}
	
	public void put(Route route, int routeId) {
		map.put(routeId, route);
	}

}
