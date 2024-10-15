package requests;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException() {
      super("Required params were not present in the request");
    }
}
