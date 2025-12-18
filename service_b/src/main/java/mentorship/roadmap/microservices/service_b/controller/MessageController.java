package mentorship.roadmap.microservices.service_b.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mentorship.roadmap.microservices.service_b.dto.MessageDto;
import mentorship.roadmap.microservices.service_b.enums.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@RestController
@RequestMapping("api/process")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final WebClient.Builder webClientBuilder;

    @Value("${service-c.uri}")
    private String serviceCUri;

    @PostMapping
    public ResponseEntity<String> processMessage(@Valid @RequestBody MessageDto messageDto) {
        try {
            log.info("Received message from Service A: {}", messageDto);
            if (MessageType.IMPORTANT.equals(messageDto.getType())) {
                //Создаем уникальный ключ для Redis
                String key = "msg:" + System.currentTimeMillis();

                // Сохраняем в Redis на 5 минут
                // redisTemplate.opsForValue() - работает с ключ-значение в Redis
                // Duration.ofMinutes(5) - время жизни 5 минут
                redisTemplate.opsForValue().set(key, messageDto, Duration.ofMinutes(5));
                log.info("Message saved ot Redis with key: {}", key);
            }

            // Вызываем Service C по REST
            // Создаем WebClient с базовым URL Service C
            WebClient webClient = webClientBuilder.baseUrl(serviceCUri).build();

            //Выполняем POST запрос к Service C
            // Асинхронный неблокирующий вызов
            webClient.post()
                    .uri("/api/save") // Добавляется к базовому URL
                    .bodyValue(messageDto) // Отправляем наше сообщение
                    .retrieve() // Получаем ответ
                    .bodyToMono(String.class) // Преобразуем тело ответа в String
                    .subscribe( // Асинхронно обрабатываем результат
                            response -> log.info("Successfully called Service C"),
                            error -> log.error("Error calling Service C", error.getMessage())
                    );

            //Возвращаем успешынй ответ
            return ResponseEntity.ok("Message processed successfully");
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error processing message");
        }
    }
}
