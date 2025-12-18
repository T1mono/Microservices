package mentorship.roadmap.microservices.service_a.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_a.dto.MessageDto;
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
    private final ObjectMapper objectMapper;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String rawJson) {
        try {
            log.info("Received raw JSON: {}", rawJson);

            // Десериализуем вручную для отладки
            MessageDto messageDto = objectMapper.readValue(rawJson, MessageDto.class);
            log.info("Parsed message: {}", messageDto);

            // Убедимся, что timestamp установлен
            if (messageDto.getTimestamp() == null) {
                messageDto.setTimestamp(LocalDateTime.now());
            }

            String jsonMessage = objectMapper.writeValueAsString(messageDto);
            log.info("Sending to Kafka: {}", jsonMessage);

            kafkaTemplate.send("in", jsonMessage);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending message: " + e.getMessage());
        }
    }
}