package mentorship.roadmap.microservices.service_c.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_c.mapper.MessageMapper;
import mentorship.roadmap.microservices.service_c.model.ProcessedMessage;
import mentorship.roadmap.microservices.service_c.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @KafkaListener(topics = "out", groupId = "service-c")
    public void listen(String message) {
        log.info("Received message: {}", message);
        try {
            //Обработка и сохранение сообщения
            ProcessedMessage processedMessage = new ProcessedMessage();
            processedMessage.setMessage(message);
            processedMessage.setType("processed");
            processedMessage.setProcessedAt(LocalDateTime.now());

            messageRepository.save(processedMessage);
            log.info("Message saved to database");
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
