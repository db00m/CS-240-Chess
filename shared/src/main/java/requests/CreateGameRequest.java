package requests;

public record CreateGameRequest(String gameName) {
    public void validate() throws InvalidRequestException {
        if (gameName == null) {
            throw new InvalidRequestException();
        }
    }
}
