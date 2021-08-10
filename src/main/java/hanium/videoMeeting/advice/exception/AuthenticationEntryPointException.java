package hanium.videoMeeting.advice.exception;

public class AuthenticationEntryPointException extends RuntimeException {
    public AuthenticationEntryPointException() {
        super();
    }

    public AuthenticationEntryPointException(String message) {
        super(message);
    }
}
