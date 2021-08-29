package hanium.videoMeeting.advice.exception;

public class ExistedEmailAndNameException extends RuntimeException {
    public ExistedEmailAndNameException() {
        super();
    }

    public ExistedEmailAndNameException(String message) {
        super(message);
    }
}
