import enums.Status;

public class Order {
    private Status status;
    private String orderDetail;
    private Customer customer;
    private Shop shop;
    private Driver driver;


    public Order(Customer customer, Shop shop, String orderDetail) {
        status = Status.NEW;
        this.customer = customer;
        this.orderDetail = orderDetail;
        this.shop = shop;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shop getShop() {
        return shop;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}


