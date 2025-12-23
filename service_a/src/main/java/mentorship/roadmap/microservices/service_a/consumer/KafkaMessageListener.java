package mentorship.roadmap.microservices.service_a.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_a.dto.MessageDto;
import mentorship.roadmap.microservices.service_a.mapper.MessageMapper;
import mentorship.roadmap.microservices.service_a.model.Message;
import mentorship.roadmap.microservices.service_a.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {

    @Value("${service-b.uri}")
    private String serviceBUrl;

    private final MessageRepository messageRepository;
    private final WebClient.Builder builder;
    private final MessageMapper messageMapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "in")
    public void listen(String message) {
        log.info("Message received: {}", message);

        try {
            // Преобразуем JSON в DTO
            MessageDto dto = objectMapper.readValue(message, MessageDto.class);
            log.info("Parsed DTO: {}", dto);

            Message entity = messageMapper.toEntity(dto);
            messageRepository.save(entity);

            WebClient webClient = builder.baseUrl(serviceBUrl).build();
            webClient.post()
                    .uri("/api/process")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> log.info("Successfully called Service B"),
                            error -> log.error("Error calling Service B: {}", error.getMessage())
                    );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
