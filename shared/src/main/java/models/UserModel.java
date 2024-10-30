package models;

import java.util.Objects;

public class UserModel {
    int ID;
    String username;
    String password;
    String email;

    public UserModel(String username, String password, String email) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserModel(String username, String email) {
        this.username = username;
        this.email = email;
        this.password = null;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserModel userModel = (UserModel) o;
        return ID == userModel.ID && Objects.equals(username, userModel.username) && Objects.equals(password, userModel.password) && Objects.equals(email, userModel.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username, password, email);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
