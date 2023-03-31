import enums.Topic;

import javax.swing.*;
import java.util.*;

public class Dispatcher {
    private static Dispatcher INSTANCE;
    private Hashtable<Order, Customer> orderCustomerTable;
    private Hashtable<Topic, List<Subscriber>> subscriberLists;
    private Map<Order, Driver> deliverTable;
    private List<Shop> shops;
    private Map<Shop, List<Subscriber>> recommendedDrivers;


    // calculate the priority of dirvers for each shop
    private List<Subscriber> rank(List<Subscriber> drivers, Shop shops) {
        return drivers;
    }

    // update the recommendedDrivers
    private void updateRecommendDrivers() {

    }


    public static Dispatcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Dispatcher();
        }
        return INSTANCE;
    }

    private Dispatcher() {
        subscriberLists = new Hashtable<>();
        orderCustomerTable = new Hashtable<>();
        deliverTable = new HashMap<>();
        shops = new ArrayList<>();
        recommendedDrivers = new HashMap<>();
    }

    public void sendMessage(Topic t, Message m) {
        switch (t) {
            case NEW_ORDER -> {
                Customer customer = (Customer) m.getContent("customer");
                Order order = (Order) m.getContent("order");
                orderCustomerTable.put(order, customer);
                order.getShop().receivedOrder(order);
            }
            case NEW_DELIVERY -> {
                Order order = (Order) m.getContent("order");
                Shop shop = order.getShop();
                List<Subscriber> drivers = recommendedDrivers.get(shop);
                for (Subscriber s : drivers) {
                    s.receivedMessage(t, m);
                }
                deliverTable.put(order, null);
            }
            case CHANGE_STATUS -> {
                Order order = (Order) m.getContent("order");
                Customer customer = orderCustomerTable.get(order);
                customer.receivedMessage(t, m);
            }
            case FEEDBACK -> {
                Order order = (Order) m.getContent("order");
                Shop shop = order.getShop();
                Driver driver = order.getDriver();
                shop.receivedMessage(t, m);
                driver.receivedMessage(t, m);
            }
        }
    }

    public void registerSubscriber(Subscriber s, Topic t) {
        if (subscriberLists.contains(t)) {
            subscriberLists.get(t).add(s);
        } else {
            List<Subscriber> subscribers = new ArrayList<>();
            subscribers.add(s);
            subscriberLists.put(t, subscribers);
        }
    }

    public Map<Order, Driver> getDeliverTable() {
        return deliverTable;
    }
}
