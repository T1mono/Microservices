package mentorship.roadmap.microservices.mapper;

import mentorship.roadmap.microservices.dto.MessageDto;
import mentorship.roadmap.microservices.enums.MessageType;
import mentorship.roadmap.microservices.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageMapper {

    public Message toEntity(MessageDto dto) {
        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setType(dto.getType().getValue());
        message.setReceivedAt(LocalDateTime.now());
        return message;
    }

    public MessageDto toDto(Message entity) {
        // При преобразовании обратно в DTO, создаем его с помощью билдера
        // и конвертируем строку обратно в enum
        return MessageDto.builder()
                .message(entity.getMessage())
                .type(MessageType.fromValue(entity.getType()))
                .build();
    }
}
