package hu.db.techshop.model;

public class OrderItem {

    private int id;
    private int orderId;
    private int productId;
    private String productNumber;
    private String productName;
    private int price;
    private int qty;

    public OrderItem() {
    }

    public OrderItem(int productId, int price, int qty) {
        this.productId = productId;
        this.price = price;
        this.qty = qty;
    }

    public OrderItem(int id, int orderId, int productId, int price, int qty) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
