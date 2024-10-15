package responses;

public record BasicResponse(String message) {
    public BasicResponse() {
        this(null);
    }
}
