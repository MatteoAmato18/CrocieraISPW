package bean;

public class BookingBean {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private String roomType;        // voce selezionata in ComboBox
    private String destinationName; // radio‑button
    private String packageName;     // radio‑button
    private String activityName;    // radio‑button

    /* ---------------- getter & setter ---------------- */
    public String getFirstName()                   { return firstName; }
    public void   setFirstName(String firstName)   { this.firstName = firstName; }

    public String getLastName()                    { return lastName; }
    public void   setLastName(String lastName)     { this.lastName = lastName; }

    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }

    public String getPhoneNumber()                 { return phoneNumber; }
    public void   setPhoneNumber(String phone)     { this.phoneNumber = phone; }

    public String getRoomType()                    { return roomType; }
    public void   setRoomType(String roomType)     { this.roomType = roomType; }

    public String getDestinationName()             { return destinationName; }
    public void   setDestinationName(String dest)  { this.destinationName = dest; }

    public String getPackageName()                 { return packageName; }
    public void   setPackageName(String pkg)       { this.packageName = pkg; }

    public String getActivityName()                { return activityName; }
    public void   setActivityName(String act)      { this.activityName = act; }
}