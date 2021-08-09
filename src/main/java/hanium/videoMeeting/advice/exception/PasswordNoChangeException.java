package hanium.videoMeeting.advice.exception;

public class PasswordNoChangeException extends RuntimeException {
    public PasswordNoChangeException() {
        super();
    }

    public PasswordNoChangeException(String message) {
        super(message);
    }
}
