package hanium.videoMeeting.advice.exception;

public class NoSuchSessionException extends RuntimeException{
    public NoSuchSessionException() {
        super();
    }

    public NoSuchSessionException(String message) {
        super(message);
    }
}
