package exception;

public class EventNameAlreadyExistsException extends RuntimeException{
    public EventNameAlreadyExistsException(String message) {
        super(message);
    }
}
