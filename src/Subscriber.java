import enums.Topic;


public interface Subscriber {
    void receivedMessage(Topic t, Message m);
}
