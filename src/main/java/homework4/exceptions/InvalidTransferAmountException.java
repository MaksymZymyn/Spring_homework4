package homework4.exceptions;

public class InvalidTransferAmountException extends RuntimeException {
    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
