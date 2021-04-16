package space.karsukova.educateapp;

public class User {

    private String email;
    private String fullName;
    private String phone;
    private String password;

    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public User(String fullName, String email,  String phone) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
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
}