package hanium.videoMeeting.advice.exception;

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException() {
        super();
    }

    public PasswordWrongException(String message) {
        super(message);
    }
}
