import enums.Topic;

public interface Publisher {
    void publish(Topic topic, Message m);
}
