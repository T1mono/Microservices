package mentorship.roadmap.microservices.service_b;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
@Slf4j
public class ServiceBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBApplication.class, args);
        log.info("=== SERVICE B WITH @EnableKafka ===");
    }

    // ПРОСТЕЙШИЙ ТЕСТ
    @KafkaListener(topics = "in", groupId = "enablekafka-test")
    public void simpleTest(String message) {
        System.out.println("!!! @EnableKafka LISTENER WORKING !!!");
        System.out.println("Message: " + message);
    }
}