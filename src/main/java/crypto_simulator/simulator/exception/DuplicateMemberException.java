package crypto_simulator.simulator.exception;

// original : https://github.com/SilverNine/spring-boot-jwt-tutorial
public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super();
    }
    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateMemberException(String message) {
        super(message);
    }
    public DuplicateMemberException(Throwable cause) {
        super(cause);
    }
}