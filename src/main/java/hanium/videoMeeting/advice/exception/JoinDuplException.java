package hanium.videoMeeting.advice.exception;

public class JoinDuplException extends RuntimeException {
    public JoinDuplException() {
        super();
    }

    public JoinDuplException(String message) {
        super(message);
    }
}
