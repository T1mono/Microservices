package mentorship.roadmap.microservices.service_c.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessedMessageResponseDto {
    private Long id;
    private String message;
    private String type;
    private LocalDateTime processedAt;
}
