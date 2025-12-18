package mentorship.roadmap.microservices.service_a.repository;

import mentorship.roadmap.microservices.service_a.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
