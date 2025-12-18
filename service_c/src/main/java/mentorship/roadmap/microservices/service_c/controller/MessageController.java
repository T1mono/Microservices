package mentorship.roadmap.microservices.service_c.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_c.dto.ProcessedMessageRequestDto;
import mentorship.roadmap.microservices.service_c.dto.ProcessedMessageResponseDto;
import mentorship.roadmap.microservices.service_c.mapper.MessageMapper;
import mentorship.roadmap.microservices.service_c.model.ProcessedMessage;
import mentorship.roadmap.microservices.service_c.repository.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Validated
public class MessageController {

    private final MessageRepository messageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageMapper messageMapper;

    @PostMapping("/save")
    public ResponseEntity<ProcessedMessageResponseDto> saveMessage(
            @Valid @RequestBody ProcessedMessageRequestDto requestDto
    ) {
        try {
            log.info("Received message from Service B: {}", requestDto);

            // Маппим DTO в Entity
            ProcessedMessage message = messageMapper.requestDtoToEntity(requestDto);

            // Сохраняем в БД
            ProcessedMessage savedMessage = messageRepository.save(message);
            log.info("Message saved to DB with ID: {}", savedMessage.getId());

            // Публикуем в Kafka topic "out"
            String kafkaMessage = String.format("ID: %d | Type: %s | Message: %s",
                    savedMessage.getId(), savedMessage.getType(), savedMessage.getMessage());
            kafkaTemplate.send("out", kafkaMessage);
            log.info("Published message to kafka topic 'out'");

            // Маппим entity обратно в dto
            ProcessedMessageResponseDto responseDto = messageMapper.entityToResponseDto(savedMessage);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
