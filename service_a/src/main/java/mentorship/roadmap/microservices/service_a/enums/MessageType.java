package mentorship.roadmap.microservices.service_a.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MessageType {
    IMPORTANT("important"),
    REGULAR("regular");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue  // При сериализации использует value
    public String getValue() {
        return value;
    }

    @JsonCreator  // При десериализации преобразует из строки
    public static MessageType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (MessageType type : MessageType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type: " + value);
    }
}