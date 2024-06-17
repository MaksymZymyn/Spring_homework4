package homework4.exceptions;

public class EmployerForCustomerNotFoundException extends RuntimeException {
    public EmployerForCustomerNotFoundException(String message) {
        super(message);
    }
}