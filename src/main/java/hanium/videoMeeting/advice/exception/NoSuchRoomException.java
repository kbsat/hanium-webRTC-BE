package hanium.videoMeeting.advice.exception;

public class NoSuchRoomException extends RuntimeException {
    public NoSuchRoomException() {
        super();
    }

    public NoSuchRoomException(String message) {
        super(message);
    }
}

