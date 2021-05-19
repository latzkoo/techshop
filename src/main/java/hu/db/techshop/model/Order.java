package hu.db.techshop.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class Order {

    private int id;

    @NotNull(message = "A mező kitöltése kötelező!")
    private int userId;

    @NotNull(message = "A mező kitöltése kötelező!")
    private Integer paymentMethodId;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shippingName;

    @NotNull(message = "A mező kitöltése kötelező!")
    private Integer shippingZip;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shippingCity;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shippingStreet;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shippingPhone;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String billingName;

    @NotNull(message = "A mező kitöltése kötelező!")
    private Integer billingZip;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String billingCity;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String billingStreet;

    private String billingTaxNumber;

    @AssertTrue(message = "Nem fogadta el az Általános Szerződési Feltételeket!")
    private boolean terms;

    private int statusId;
    private Timestamp createdAt;
    private String paymentMethod;
    private String firstname;
    private String lastname;
    private int productCount;
    private List<OrderItem> items;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public Integer getShippingZip() {
        return shippingZip;
    }

    public void setShippingZip(Integer shippingZip) {
        this.shippingZip = shippingZip;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public Integer getBillingZip() {
        return billingZip;
    }

    public void setBillingZip(Integer billingZip) {
        this.billingZip = billingZip;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingStreet() {
        return billingStreet;
    }

    public void setBillingStreet(String billingStreet) {
        this.billingStreet = billingStreet;
    }

    public String getBillingTaxNumber() {
        return billingTaxNumber;
    }

    public void setBillingTaxNumber(String billingTaxNumber) {
        this.billingTaxNumber = billingTaxNumber;
    }

    public boolean isTerms() {
        return terms;
    }

    public void setTerms(boolean terms) {
        this.terms = terms;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
