package mentorship.roadmap.microservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @RequestMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto) {
        try {
            log.info("Received message: {}", messageDto);
            String jsonMessage = objectMapper.writeValueAsString(messageDto);

            //Отправляем в сообщения в topic "in"
            kafkaTemplate.send("in", jsonMessage);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }
}
