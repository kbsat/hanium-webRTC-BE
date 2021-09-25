package hanium.videoMeeting.advice.exception;

public class NotWorkingExitException extends RuntimeException {
    public NotWorkingExitException() {
        super();
    }

    public NotWorkingExitException(String message) {
        super(message);
    }
}
