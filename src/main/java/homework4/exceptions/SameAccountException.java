package homework4.exceptions;

public class SameAccountException extends RuntimeException {
    public SameAccountException(String message) {
        super(message);
    }
}
