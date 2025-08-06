package entity;





public class User extends Client {
	private String phoneNumber;

    public User() {}

    public User(String firstName, String lastName, String email, String phoneNumber, String password) {
        super(firstName, lastName, email,  password);
        
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getUserType() {
        return "user";
    }
}