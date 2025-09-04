package mentorship.roadmap.microservices.service_c.mapper;

import mentorship.roadmap.microservices.service_c.dto.ProcessedMessageRequestDto;
import mentorship.roadmap.microservices.service_c.dto.ProcessedMessageResponseDto;
import mentorship.roadmap.microservices.service_c.model.ProcessedMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {


    public ProcessedMessage requestDtoToEntity(ProcessedMessageRequestDto requestDto) {
        ProcessedMessage processedMessage = new ProcessedMessage();
        processedMessage.setMessage(requestDto.getMessage());
        processedMessage.setType(requestDto.getType());
        processedMessage.setProcessedAt(LocalDateTime.now());
        return processedMessage;
    }

    public ProcessedMessageResponseDto entityToResponseDto(ProcessedMessage entity) {
        ProcessedMessageResponseDto dto = new ProcessedMessageResponseDto();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setType(entity.getType());
        dto.setProcessedAt(entity.getProcessedAt());
        return dto;
    }
}
