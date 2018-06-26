package it.polito.tdp.flight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.Airline;
import it.polito.tdp.flight.model.Airport;
import it.polito.tdp.flight.model.Model;
import it.polito.tdp.flight.model.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FlightController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Airline> boxAirline;

    @FXML
    private ComboBox<Airport> boxAirport;

    @FXML
    private TextArea txtResult;

    @FXML
    void doRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	if(model.getGrafo() == null) {
    		txtResult.setText("Selezionare una compagnia aerea e quindi il tasto \"AeroportiServeriti\".");
    		return;
    	}
    	Airport airport = boxAirport.getValue();
    	if(airport == null) {
    		txtResult.setText("Selezionare un aeroporto dall'elenco,\nSe elenco vuoto, selezionare un altra compagnia aerea.");
    		return;
    	}
    	List<Result> raggiunti = model.getRaggiungibiliFromAirport(airport);
    	txtResult.appendText("Aeroporti raggiungibili da "+airport.toString()+": \n");
    	for(Result r : raggiunti) {
    		txtResult.appendText(r.toString()+"\n");
    	}
    	boxAirport.getItems().clear();
    	model.clear();
    	
    }

    @FXML
    void doServiti(ActionEvent event) {
    	txtResult.clear();
    	Airline airline = boxAirline.getSelectionModel().getSelectedItem();
    	if(airline == null) {
    		txtResult.setText("Selezionare una compagnia aerea.");
    		return;
    	}
    	boxAirport.getItems().addAll(model.getAirportsByAirline(airline));
    	model.creaGrafo(airline);
    }

    @FXML
    void initialize() {
        assert boxAirline != null : "fx:id=\"boxAirline\" was not injected: check your FXML file 'Flight.fxml'.";
        assert boxAirport != null : "fx:id=\"boxAirport\" was not injected: check your FXML file 'Flight.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Flight.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxAirline.getItems().addAll(model.getAirlines());
		
		
	}
}
