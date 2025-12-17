package mentorship.roadmap.microservices.service_b.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentorship.roadmap.microservices.service_b.enums.MessageType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    @NotBlank(message = "Type cannot be blank")
    private MessageType type;  // Используем enum вместо String

    private LocalDateTime timestamp;

    @Builder
    public MessageDto(String message, MessageType type, LocalDateTime localDateTime) {
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
