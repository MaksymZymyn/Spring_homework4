package homework4.exceptions;

/**
 * Исключение используется для ошибок аутентификации и авторизации.
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

}
