package models;


public record UserModel(Integer ID, String username, String password, String email) {

    public UserModel(String username, String password, String email) {
        this(null, username, password, email);
    }
}
