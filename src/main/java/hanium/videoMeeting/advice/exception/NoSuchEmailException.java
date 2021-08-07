package hanium.videoMeeting.advice.exception;

public class NoSuchEmailException extends RuntimeException {
    public NoSuchEmailException() {
        super();
    }

    public NoSuchEmailException(String message) {
        super(message);
    }
}
