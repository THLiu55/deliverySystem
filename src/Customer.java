import enums.Topic;

public class Customer implements Subscriber, Publisher {
    private float[] location;

    public Customer() {
        // Customer 作为subscriber可以接收Topic为 "CHANGE_STATUS" 的内容
        Dispatcher.getInstance().registerSubscriber(this, Topic.CHANGE_STATUS);
    }

    // 创建订单
    public void order(Shop shop, String content) {
        Order order =  new Order(this, shop, content);
        publishOrder(order);
    }

    // 用户反馈
    public void feedback(String content, Order order) {
        Message m = new Message();
        m.addItem("content", content);
        m.addItem("order", order);
        publish(Topic.FEEDBACK, m);
    }

    public void changeOrderStatus() {

    }

    // 发布订单
    public void publishOrder(Order order) {
        Message message = new Message();
        message.addItem("order", order);
        message.addItem("customer", this);
        publish(Topic.NEW_ORDER, message);
    }

    // 接收订单状态信息改变
    @Override
    public void receivedMessage(Topic t, Message m) {
        switch (t) {
            case CHANGE_STATUS -> changeOrderStatus();
        }
    }

    @Override
    public void publish(Topic topic, Message m) {
        Dispatcher.getInstance().sendMessage(topic, m);
    }

    public float[] getLocation() {
        return location;
    }
}
