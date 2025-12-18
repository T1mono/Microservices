package mentorship.roadmap.microservices.service_b.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_b.dto.MessageDto;
import mentorship.roadmap.microservices.service_b.enums.MessageType;
import mentorship.roadmap.microservices.service_b.exception.MessageProcessingException;
import mentorship.roadmap.microservices.service_b.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {
    private final RedisTemplate<String, String> redisTemplate;
    private final MessageMapper messageMapper;
    private final WebClient.Builder webClientBuilder; //Для вызова сервиса service_c

    //TODO
    @Value("${service_c.url}")
    private String serviceCUrl;

    @KafkaListener(
            topics = "in",
            groupId = "service-b",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(String message) {
        log.info("Received message from topic 'in': {}", message);

        try {
            // Парсинг сообщения
            MessageDto messageDto = messageMapper.stringToDto(message);

            // Проверка типа сообщения
            if (MessageType.IMPORTANT.equals(messageDto.getType())) {
                // Сохранение в Redis с TTL
                String key = "message:" + System.currentTimeMillis();
                redisTemplate.opsForValue().set(key, messageDto.getMessage(), Duration.ofMinutes(5));
                log.info("Saved important message to Redis with key: {}", key);
            } else {
                log.debug("Skipping non-important message: {}", messageDto.getType());
            }

            //Вызов service_c
            WebClient webClient = webClientBuilder.baseUrl(serviceCUrl).build();
            webClient.post()
                    .uri("/api/save")
                    .bodyValue(messageDto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> log.info("Message sent to service_c: {}", response),
                            error -> log.error("Error calling Service C: {}", error.getMessage())
                    );


        } catch (JsonProcessingException e) {
            log.error("Failed to parse message: {}", message, e);
            throw new MessageProcessingException("Invalid message format", e);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw new MessageProcessingException("Failed to process message", e);
        }
    }
}
