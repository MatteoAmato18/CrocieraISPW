package entity;

public class Booking {

    private int    id;

    /* --- dati anagrafici del cliente --- */
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    /* --- scelte di prenotazione --- */
    private String roomType;        // chiave della Room
    private String destinationName; // chiave di Destination
    private String packageName;     // chiave di CruisePackage
    private String activityName;    // chiave di Activity (una sola)

    private BookingStatus status = BookingStatus.PENDING;

    /* ------------------------------ getter & setter ------------------------------ */
    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getFirstName()             { return firstName; }
    public void setFirstName(String first)   { this.firstName = first; }

    public String getLastName()              { return lastName; }
    public void setLastName(String last)     { this.lastName = last; }

    public String getEmail()                 { return email; }
    public void setEmail(String email)       { this.email = email; }

    public String getPhoneNumber()           { return phoneNumber; }
    public void setPhoneNumber(String phone) { this.phoneNumber = phone; }

    public String getRoomType()              { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getDestinationName()              { return destinationName; }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getPackageName()           { return packageName; }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName()          { return activityName; }
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BookingStatus getStatus()         { return status; }
    public void setStatus(BookingStatus s)   { this.status = s; }

    @Override public String toString() {
        return "Booking[" + id + ", " + firstName + " " + lastName +
               ", room=" + roomType + ", dest=" + destinationName +
               ", pkg=" + packageName + ", act=" + activityName +
               ", status=" + status + "]";
    }
}