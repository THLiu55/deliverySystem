import java.util.HashMap;

public class Message {
    private HashMap<String, Object> content;

    public Message() {
        content = new HashMap<>();
    }

    public void addItem(String key, Object item) {
        content.put(key, item);
    }

    public Object getContent(String key) {
        return content.get(key);
    }

    public void setContent(String key, Object item) {
        content.put(key, item);
    }
}
