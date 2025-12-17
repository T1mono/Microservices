package mentorship.roadmap.microservices.service_b.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageProcessingException extends RuntimeException {
    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
