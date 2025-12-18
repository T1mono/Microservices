package mentorship.roadmap.microservices.service_b.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_b.dto.MessageDto;
import mentorship.roadmap.microservices.service_b.enums.MessageType;
import mentorship.roadmap.microservices.service_b.exception.MessageProcessingException;
import mentorship.roadmap.microservices.service_b.mapper.MessageMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {
    private final RedisTemplate<String, String> redisTemplate;
    private final MessageMapper messageMapper;

    @KafkaListener(
            topics = "in",
            groupId = "service-b",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        log.info("Received message from topic 'out': {}", message);

        try {
            // Парсинг сообщения
            MessageDto messageDto = messageMapper.stringToDto(message);

            // Проверка типа сообщения
            if (MessageType.IMPORTANT.equals(messageDto.getType())) {
                // Сохранение в Redis с TTL
                String key = "message:" + messageDto.getId();
                redisTemplate.opsForValue().set(key, messageDto.getMessage(), Duration.ofHours(1));
                log.info("Saved important message to Redis with key: {}", key);
            } else {
                log.debug("Skipping non-important message: {}", messageDto.getType());
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse message: {}", message, e);
            throw new MessageProcessingException("Invalid message format", e);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw new MessageProcessingException("Failed to process message", e);
        }
    }
}
