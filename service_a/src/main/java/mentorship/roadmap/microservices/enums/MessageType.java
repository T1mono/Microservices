package mentorship.roadmap.microservices.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    IMPORTANT("important"),
    REGULAR("regular");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType fromValue(String value) {
        for (MessageType type : MessageType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + value);
    }
}
