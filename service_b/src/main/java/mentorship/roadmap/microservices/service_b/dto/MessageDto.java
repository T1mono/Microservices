package mentorship.roadmap.microservices.service_b.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MessageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 character")
    private String message;

    @NotBlank(message = "Type cannot be blank")
    @Size(min = 1, max = 30, message = "Type must be between 1 and 30 character")
    private String type;
}
