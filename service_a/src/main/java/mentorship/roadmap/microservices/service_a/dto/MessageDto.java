package mentorship.roadmap.microservices.service_a.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentorship.roadmap.microservices.service_a.enums.MessageType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    private MessageType type;

    private LocalDateTime timestamp;

    // Явный конструктор для Jackson
    @JsonCreator
    public static MessageDto create(
            @JsonProperty("message") String message,
            @JsonProperty("type") String type) {
        return MessageDto.builder()
                .message(message)
                .type(MessageType.fromValue(type.toUpperCase()))
                .timestamp(LocalDateTime.now())
                .build();
    }
}