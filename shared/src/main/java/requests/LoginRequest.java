package requests;

public record LoginRequest(String username, String password) {
    public void validate() throws InvalidRequestException {
        if ((username == null) || (password == null)) {
            throw new InvalidRequestException();
        }
    }
}
