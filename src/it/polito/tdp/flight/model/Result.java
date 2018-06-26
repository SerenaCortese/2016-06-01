package it.polito.tdp.flight.model;

public class Result implements Comparable<Result>{
	
	private Airport dest;
	private double distanza;
	public Result(Airport dest, double distanza) {
		super();
		this.dest = dest;
		this.distanza = distanza;
	}
	public Airport getDest() {
		return dest;
	}
	public void setDest(Airport dest) {
		this.dest = dest;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(Result altro) {
		return Double.compare(this.distanza, altro.distanza);
	}
	@Override
	public String toString() {
		return "" + dest + " distanza: " + distanza;
	}
	
	
	
	

}
