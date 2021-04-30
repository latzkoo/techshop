package hu.db.techshop.model;

public class PaymentMethod {

    private int id;
    private String paymentMethod;

    public PaymentMethod() {
    }

    public PaymentMethod(int id, String paymentMethod) {
        this.id = id;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
