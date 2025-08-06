package entity;

/**
 * Destinazione della crociera (itinerario o porto principale).
 */
public class Destination {

    private String name;   // es. "Caraibi", "Mediterraneo Orientale"
    private double cost;   // prezzo base della tratta

    public Destination() { }

    public Destination(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    /* --- getter & setter --- */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Destination{" +
               "name='" + name + '\'' +
               ", cost=" + cost +
               '}';
    }
}
