package hanium.videoMeeting.advice.exception;

public class ReservationTimeMisMatchException extends RuntimeException{
    public ReservationTimeMisMatchException() {
        super();
    }

    public ReservationTimeMisMatchException(String message) {
        super(message);
    }
}
