package models;


import java.util.Objects;

public record UserModel(Integer id, String username, String password, String email) {

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
        return Objects.equals(id, userModel.id) && Objects.equals(email, userModel.email) && Objects.equals(username, userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
