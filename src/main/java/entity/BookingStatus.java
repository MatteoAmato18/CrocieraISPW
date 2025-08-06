package entity;

/** Stati possibili di una prenotazione. */
public enum BookingStatus {
    PENDING,        // appena creata, in attesa di conferma
    BOOKED,         // confermata
    CANCELLED        // annullata
}