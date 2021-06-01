package space.karsukova.educateapp.utils;

public class Post {
    private String date, postDesc, postImageUrl, userProfileImageUrl, username;

    public Post(String date, String postDesc, String postImageUrl, String userProfileImageUrl, String username) {
        this.date = date;
        this.postDesc = postDesc;
        this.postImageUrl = postImageUrl;
        this.userProfileImageUrl = userProfileImageUrl;
        this.username = username;
    }

    public Post() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
