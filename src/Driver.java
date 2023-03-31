import enums.Status;
import enums.Topic;

import java.util.ArrayList;
import java.util.List;

public class Driver implements Subscriber, Publisher {
    private List<Order> claimedOrders;
    private Topic topic;
    private float[] location;
    private double successDeliverAmount;
    private double totalDeliverAmount;
    private double successfulRate;

    public Driver() {
        successDeliverAmount = 0;
        totalDeliverAmount = 0;
        successfulRate = 1d;
        claimedOrders = new ArrayList<>();
        // Driver 作为publisher可以发布Topic为 "CHANGE_STATUS" 的内容
        topic = Topic.CHANGE_STATUS;
        // Driver 作为subscriber可以接收Topic为 "NEW_ORDER" 的内容
        Dispatcher.getInstance().registerSubscriber(this, Topic.NEW_ORDER);
    }

    private void updateLocation() {}

    public float[] getLocation() {
        return location;
    }

    private boolean claimOrNot(Order order) {
        // 这里会弹出一个弹窗询问driver是否接单, 若接单返回True，否则返回False
        // 只有做出选择函数才会结束
        return true;
    }


    private void notFirstNotify() {
        // 弹窗通知未抢到单
    }

    public void feedbackNotify() {

    }

    // 改变状态到"接单"
    public void changeStatus2Claimed(Order order) {
        order.setStatus(Status.CLAIMED_BY_DRIVER);
        Message message = new Message();
        message.addItem("order", order);
        publish(Topic.CHANGE_STATUS, message);
    }

    // 改变状态到"再送餐"
    public void changeStatus2Delivering(Order order) {
        order.setStatus(Status.DELIVERING);
        Message message = new Message();
        message.addItem("order", order);
        publish(Topic.CHANGE_STATUS, message);
    }

    // 改变状态到"已送达"
    public void changeStatus2Delivered(Order order) {
        order.setStatus(Status.DELIVERED);
        Message message = new Message();
        message.addItem("order", order);
        updateSuccessfulRate();
        publish(Topic.CHANGE_STATUS, message);
    }


    private void updateSuccessfulRate() {
        successDeliverAmount++;
        totalDeliverAmount++;
        successfulRate = successDeliverAmount / totalDeliverAmount;
    }


    @Override
    public void receivedMessage(Topic t, Message m) {
        switch (t) {
            case NEW_ORDER: {
                // 异步询问骑手是否接单
                new Thread(()->{
                    Order order = (Order) m.getContent("order");
                    boolean claim = claimOrNot(order);
                    // 若骑手接单，将订单加入骑手的 claimedOrders
                    if (claim) {
                        // check if I'm the first one
                        Driver first = Dispatcher.getInstance().getDeliverTable().get(order);
                        if (first == null) {
                            order.setDriver(this);
                            claimedOrders.add(order);
                            changeStatus2Claimed(order);
                        } else {
                            notFirstNotify();
                        }
                    } else {
                        order.setStatus(Status.DENIED);
                        m.addItem("order", order);
                        publish(Topic.CHANGE_STATUS, m);
                    }
                }).start();
            }
            case FEEDBACK: {
                successDeliverAmount--;
                successfulRate = successDeliverAmount / totalDeliverAmount;
                feedbackNotify();
            }
        }
    }

    @Override
    public void publish(Topic topic, Message m) {
        Dispatcher.getInstance().sendMessage(topic, m);
    }
}
