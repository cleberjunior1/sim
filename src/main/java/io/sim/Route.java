package io.sim;

public class Route {
    // adicionar construtor V
    // adicionar destrutor
    // adicionar atributos privados de rotas -> edges
    private Itinerary itinerario;

    public Route(String idRota) {
        this.itinerario = new Itinerary("data/dados2.xml", idRota);
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