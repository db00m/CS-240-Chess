package models;


import java.util.Objects;

public record UserModel(Integer ID, String username, String password, String email) {

    public UserModel(String username, String password, String email) {
        this(null, username, password, email);
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
        return Objects.equals(ID, userModel.ID) && Objects.equals(email, userModel.email) && Objects.equals(username, userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username, email);
    }
}
