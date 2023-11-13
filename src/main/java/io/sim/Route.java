package io.sim;

public class Route {

    private Itinerary itinerario; // itinerário da rota

    public Route(String idRota) {
        this.itinerario = new Itinerary("data/dados2.xml", idRota); // cria o itinerário
    }

    public String getIdRota() {
        return itinerario.getIDItinerary(); // retorna o id da rota
    }

    public String getEdge() {
        return itinerario.getItinerary()[1]; // retorna o edge
    }

    public Itinerary getItinerario() {
        return this.itinerario;
    }

}