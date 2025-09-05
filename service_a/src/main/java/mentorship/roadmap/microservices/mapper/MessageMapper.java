package mentorship.roadmap.microservices.mapper;

import mentorship.roadmap.microservices.dto.MessageDto;
import mentorship.roadmap.microservices.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public Message toEntity(MessageDto dto) {
        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setType(dto.getType());
        message.setReceivedAt(LocalDateTime.now());
        return message;
    }

    public MessageDto toDto(Message entity) {
        MessageDto dto = new MessageDto();
        dto.setMessage(entity.getMessage());
        dto.setType(entity.getType());
        return dto;
    }
}
