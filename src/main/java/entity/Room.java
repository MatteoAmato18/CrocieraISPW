package entity;

/**
 * Rappresenta una tipologia di cabina/stanza a bordo.
 */
public class Room {

    private String type;          // es. "Suite", "Balcony", "Interior"
    private double cost;          // costo per l’intera crociera
    private String description;   // descrizione della stanza
    private int maxGuests;        // numero massimo di ospiti ammessi
    private RoomStatus status;

    public Room() {this.status = RoomStatus.FREE; }

    public Room(String type, double cost, String description, int maxGuests, RoomStatus status) {
        this.type = type;
        this.cost = cost;
        this.description = description;
        this.maxGuests = maxGuests;
        this.status      = status;
    }

    /* --- getter & setter --- */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
               "type='" + type + '\'' +
               ", cost=" + cost +
               ", description='" + description + '\'' +
               ", maxGuests=" + maxGuests +
               ", status=" + status +
               '}';
    }
}