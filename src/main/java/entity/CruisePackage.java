package entity;

/**
 * Rappresenta un pacchetto di servizi a bordo, ad esempio
 * "All Inclusive", "Only Beverage", "Spa Access", ecc.
 */
public class CruisePackage {

    private String name;   // nome del pacchetto
    private double cost;   // costo aggiuntivo per persona o per cabina

    public CruisePackage() { }

    public CruisePackage(String name, double cost) {
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
        return "CruisePackage{" +
               "name='" + name + '\'' +
               ", cost=" + cost +
               '}';
    }
}
