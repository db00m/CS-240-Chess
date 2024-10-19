package responses;

public record CreateGameResponse(Integer gameID, String message) {
    public CreateGameResponse(Integer gameID) {
        this(gameID, null);
    }

    public CreateGameResponse(String message) {
        this(null, message);
    }
}
