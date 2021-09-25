package hanium.videoMeeting.advice.exception;

public class NoSuchJoinRoomException extends RuntimeException{
    public NoSuchJoinRoomException() {
        super();
    }

    public NoSuchJoinRoomException(String message) {
        super(message);
    }
}
