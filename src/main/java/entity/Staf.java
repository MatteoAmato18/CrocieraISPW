package entity;

public class Staf extends Client {

    public Staf() {}
    public Staf(String firstName, String lastName, String email,  String password) {
        super(firstName, lastName, email,  password);
    }
    @Override
    public String getUserType() {
        return "staf";
    }
}