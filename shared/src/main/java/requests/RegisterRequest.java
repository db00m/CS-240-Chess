package requests;

public record RegisterRequest(String username, String password, String email) {
    public void validate() throws InvalidRequestException {
        if ((username == null) || (password == null) || (email == null)) {
            throw new InvalidRequestException();
        }
    }
}
