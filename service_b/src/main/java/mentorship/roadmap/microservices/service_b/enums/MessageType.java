package mentorship.roadmap.microservices.service_b.enums;

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
        if (value == null) {
            return REGULAR;
        }
        String lowerValue = value.toLowerCase();
        for (MessageType type : MessageType.values()) {
            if (type.value.equals(lowerValue)) {
                return type;
            }
        }
        // Если не нашли - возвращаем REGULAR по умолчанию
        return REGULAR;
    }
}
