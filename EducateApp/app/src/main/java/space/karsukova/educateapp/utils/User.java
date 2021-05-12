package space.karsukova.educateapp.utils;

public class User {

    private String email;
    private String fullName;
    private String phone;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public User(String fullName, String email,  String phone, boolean isAdmin) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }



    public void setpassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAdmin(boolean isAdmin) {this.isAdmin = isAdmin; }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getpassword() {
        return password;
    }

    public boolean getAdmin() {return isAdmin; }
}