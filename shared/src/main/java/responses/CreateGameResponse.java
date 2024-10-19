package responses;

public record CreateGameResponse(Integer gameId, String message) {
    public CreateGameResponse(Integer gameId) {
        this(gameId, null);
    }

    public CreateGameResponse(String message) {
        this(null, message);
    }
}
