package mentorship.roadmap.microservices.service_b.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mentorship.roadmap.microservices.service_b.dto.MessageDto;
import mentorship.roadmap.microservices.service_b.enums.MessageType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageMapper {
    private final ObjectMapper objectMapper;

    public MessageMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MessageDto stringToDto(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, MessageDto.class);
    }

    public String dtoToString(MessageDto messageDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageDto);
    }

    public Map<String, String> dtoToRedisMap(MessageDto messageDto) {
        Map<String, String> redisMap = new HashMap<>();
        redisMap.put("message", messageDto.getMessage());
        redisMap.put("type", messageDto.getType().getValue());
        redisMap.put("timestamp", messageDto.getTimestamp().toString());
        return redisMap;
    }

    public MessageDto redisMapToDto(Map<String, String> redisMap) {
        return new MessageDto(
                redisMap.get("message"),
                MessageType.fromValue(redisMap.getOrDefault("type", MessageType.REGULAR.getValue())),
                LocalDateTime.parse(redisMap.get("timestamp"))
        );
    }
}

