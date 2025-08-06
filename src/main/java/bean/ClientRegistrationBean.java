package bean;


public class ClientRegistrationBean {

    /* ------------------ campi ------------------ */
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String repeatPassword;
    private String userType;          // "user" | "staf"

    private static final String EMAIL_RX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /* -------------- mini-validazione ------------ */
    public boolean hasValidFirstName()   { return firstName   != null && !firstName.isBlank(); }
    public boolean hasValidLastName()    { return lastName    != null && !lastName.isBlank(); }
    public boolean hasValidEmail()       { return email       != null && email.matches(EMAIL_RX); }
    public boolean hasValidPhone()       { return phoneNumber != null && phoneNumber.length() == 10; }
    public boolean hasValidPassword()    { return password    != null && password.length() >= 8 && password.length() <= 16; }
    public boolean passwordsMatch()      { return password    != null && password.equals(repeatPassword); }
    public boolean hasUserType()         { return userType    != null && !userType.isBlank(); }

    /* ---------------- getter / setter ----------- */
    public String getFirstName()        { return firstName; }
    public void   setFirstName(String s){ firstName = s; }

    public String getLastName()         { return lastName; }
    public void   setLastName(String s) { lastName = s; }

    public String getEmail()            { return email; }
    public void   setEmail(String s)    { email = s; }

    public String getPhoneNumber()      { return phoneNumber; }
    public void   setPhoneNumber(String s){ phoneNumber = s; }

    public String getPassword()         { return password; }
    public void   setPassword(String s) { password = s; }

    public String getRepeatPassword()   { return repeatPassword; }
    public void   setRepeatPassword(String s){ repeatPassword = s; }

    public String getUserType()         { return userType; }
    public void   setUserType(String s) { userType = s; }
}