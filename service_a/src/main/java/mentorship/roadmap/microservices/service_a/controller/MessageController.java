package mentorship.roadmap.microservices.service_a.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_a.dto.MessageDto;
import mentorship.roadmap.microservices.service_a.model.Message;
import mentorship.roadmap.microservices.service_a.service.MongoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MongoService mongoService;
    private final ObjectMapper objectMapper;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto) {
        try {
            log.info("=== STEP 1: Received message ===");
            log.info("Message: {}, Type: {}", messageDto.getMessage(), messageDto.getType());

            // 1. Сохранить в MongoDB
            log.info("=== STEP 2: Saving to MongoDB ===");
            Message savedMessage = mongoService.saveToMongo(messageDto);
            log.info("Saved to MongoDB with ID: {}", savedMessage.getId());

            // 2. Отправить в Kafka
            log.info("=== STEP 3: Sending to Kafka ===");
            String jsonMessage = objectMapper.writeValueAsString(messageDto);
            kafkaTemplate.send("in", jsonMessage);

            log.info("=== STEP 4: Success ===");
            return ResponseEntity.ok("Message saved to MongoDB and sent to Kafka");

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}