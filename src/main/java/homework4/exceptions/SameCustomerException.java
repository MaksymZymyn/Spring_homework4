package homework4.exceptions;

public class SameCustomerException extends RuntimeException {
    public SameCustomerException(String message) {
        super(message);
    }
}