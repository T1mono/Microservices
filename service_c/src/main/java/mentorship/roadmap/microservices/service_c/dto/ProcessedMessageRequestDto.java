package mentorship.roadmap.microservices.service_c.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ProcessedMessageRequestDto {

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    @NotBlank(message = "Type cannot be blank")
    @Size(min = 1, max = 30, message = "Type must be between 1 and 30 characters")
    private String type;
}
