package bean;




public class LoginBean {
    private String email;
    private String password;
    private String userType;
    

    public LoginBean() {
        // Costruttore vuoto per inizializzazione graduale
    }

    private static final String EMAIL_RX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    

    /* --- mini-validazione sintattica --------------------------- */
    public boolean hasValidEmail()    { return email != null && email.matches(EMAIL_RX); }
    public boolean hasValidPassword() { return password != null && !password.isBlank(); }
    public boolean hasUserType()      { return userType != null && !userType.isBlank(); }


    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}