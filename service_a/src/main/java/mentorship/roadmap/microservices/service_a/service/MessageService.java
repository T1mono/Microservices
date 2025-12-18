package mentorship.roadmap.microservices.service_a.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_a.dto.MessageDto;
import mentorship.roadmap.microservices.service_a.mapper.MessageMapper;
import mentorship.roadmap.microservices.service_a.model.Message;
import mentorship.roadmap.microservices.service_a.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Message saveToMongo(MessageDto messageDto) {
        try {
            log.info("Saving message to MongoDB: {}", messageDto.getMessage());

            Message message = messageMapper.toEntity(messageDto);
            Message savedMessage = messageRepository.save(message);

            log.info("Message saved to MongoDB with id: {}", savedMessage.getId());
            return savedMessage;

        } catch (Exception e) {
            log.error("Error saving to MongoDB: {}", e.getMessage(), e);
            throw e;
        }
    }
}
