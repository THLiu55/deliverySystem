import enums.Status;
import enums.Topic;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Publisher, Subscriber {
    private int id;
    private float[] location;
    private List<Order> orderList;
    private Topic topic;
    private List<Order> claimedOrders;

    public Shop() {
        claimedOrders = new ArrayList<>();
        // Shop 作为publisher可以发布Topic为 "CHANGE_STATUS" 的内容
        topic = Topic.CHANGE_STATUS;
    }

    private boolean claimOrNot(Order order) {
        // 这里会弹出一个弹窗询问Restaurant是否接单, 若接单返回True，否则返回False
        // 只有做出选择函数才会结束
        return true;
    }

    // 改变状态到"接单"
    public Message changeStatus2Claimed(Order order) {
        order.setStatus(Status.CLAIMED_BY_RESTAURANT);
        Message m = new Message();
        m.setContent("order", order);
        publish(Topic.CHANGE_STATUS, m);
        return m;
    }

    public void receivedOrder(Order order) {
        // 异步询问店家是否接单
            boolean claim = claimOrNot(order);  // here is blocked
            // 若店家接单，将订单加入店家的 claimedOrders
            if (claim) {
                claimedOrders.add(order);
                Message m = changeStatus2Claimed(order);
                publish(Topic.NEW_DELIVERY, m);
            } else {
                Message m = new Message();
                order.setStatus(Status.DENIED);
                m.addItem("order", order);
                publish(Topic.CHANGE_STATUS, m);
            }
    }


    @Override
    public void publish(Topic topic, Message m) {
        Dispatcher.getInstance().sendMessage(topic, m);
    }

    public float[] getLocation() {
        return location;
    }

    public void handleFeedback() {

    }

    @Override
    public void receivedMessage(Topic t, Message m) {
        if (t == Topic.FEEDBACK) {
            handleFeedback();
        }
    }
}
