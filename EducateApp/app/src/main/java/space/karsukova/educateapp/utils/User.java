package space.karsukova.educateapp.utils;

public class User {

    private String UserEmail;
    private String FullName;
    private String PhoneNumber;
    private boolean isAdmin;
    private String Id;
    private String UserIcon;

    public User() {}


    public User(String Id, String FullName,   String PhoneNumber, String UserEmail, String UserIcon) {
        this.UserEmail = UserEmail;
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;
        this.UserIcon = UserIcon;
        this.Id = Id;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getFullName() {
        return FullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getId() {
        return Id;
    }

    public String getUserIcon() {
        return UserIcon;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUserIcon(String userIcon) {
        UserIcon = userIcon;
    }
}