package mentorship.roadmap.microservices.service_b.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_b.dto.MessageDto;
import mentorship.roadmap.microservices.service_b.enums.MessageType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageMapper {
    private final ObjectMapper objectMapper;

    public MessageDto stringToDto(String jsonMessage) throws JsonProcessingException {
        log.debug("Parsing JSON: {}", jsonMessage);

        // Парсим в JsonNode
        JsonNode node = objectMapper.readTree(jsonMessage);

        // Создаем DTO
        MessageDto dto = new MessageDto();
        dto.setMessage(node.get("message").asText());

        // Обрабатываем type
        String typeStr = node.get("type").asText();
        try {
            dto.setType(MessageType.fromValue(typeStr.toLowerCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Unknown message type: {}, defaulting to REGULAR", typeStr);
            dto.setType(MessageType.REGULAR);
        }

        // Timestamp
        dto.setTimestamp(LocalDateTime.now());

        return dto;
    }

    public String dtoToString(MessageDto messageDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageDto);
    }
}

