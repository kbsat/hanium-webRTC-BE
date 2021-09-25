package hanium.videoMeeting.advice.exception;

public class NoRoomPasswordException extends RuntimeException {
    public NoRoomPasswordException() {
        super();
    }

    public NoRoomPasswordException(String message) {
        super(message);
    }
}
