package mentorship.roadmap.microservices.dto;// Добавьте импорты

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import mentorship.roadmap.microservices.enums.MessageType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MessageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // <-- Добавлено
    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    @NotBlank(message = "Type cannot be blank")
    private MessageType type; // <-- Изменено на enum

    private LocalDateTime timestamp; // <-- Добавлено

    @Builder
    public MessageDto(String message, MessageType type) {
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now(); // Устанавливаем timestamp при создании
    }
}
