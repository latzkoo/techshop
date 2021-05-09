package hu.db.techshop.model;

import java.sql.Timestamp;

public class Invoice {

    private int id;
    private int orderId;
    private Timestamp invoiceDate;
    private Timestamp deliveryDate;
    private Timestamp dueDate;
    private int paymentMethodId;
    private String supplierName;
    private int supplierZip;
    private String supplierCity;
    private String supplierStreet;
    private String supplierVat;
    private String supplierBankAccount;
    private String supplierPhone;
    private String customerName;
    private int customerZip;
    private String customerCity;
    private String customerStreet;
    private String customerVat;
    private String invoiceComment;
    private int originalInvoiceId;
    private String paymentMethod;
    private int itemCount;
    private int invoiceValue;

    public Invoice() {
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

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getSupplierZip() {
        return supplierZip;
    }

    public void setSupplierZip(int supplierZip) {
        this.supplierZip = supplierZip;
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity;
    }

    public String getSupplierStreet() {
        return supplierStreet;
    }

    public void setSupplierStreet(String supplierStreet) {
        this.supplierStreet = supplierStreet;
    }

    public String getSupplierVat() {
        return supplierVat;
    }

    public void setSupplierVat(String supplierVat) {
        this.supplierVat = supplierVat;
    }

    public String getSupplierBankAccount() {
        return supplierBankAccount;
    }

    public void setSupplierBankAccount(String supplierBankAccount) {
        this.supplierBankAccount = supplierBankAccount;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerZip() {
        return customerZip;
    }

    public void setCustomerZip(int customerZip) {
        this.customerZip = customerZip;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerStreet() {
        return customerStreet;
    }

    public void setCustomerStreet(String customerStreet) {
        this.customerStreet = customerStreet;
    }

    public String getCustomerVat() {
        return customerVat;
    }

    public void setCustomerVat(String customerVat) {
        this.customerVat = customerVat;
    }

    public String getInvoiceComment() {
        return invoiceComment;
    }

    public void setInvoiceComment(String invoiceComment) {
        this.invoiceComment = invoiceComment;
    }

    public int getOriginalInvoiceId() {
        return originalInvoiceId;
    }

    public void setOriginalInvoiceId(int originalInvoiceId) {
        this.originalInvoiceId = originalInvoiceId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(int invoiceValue) {
        this.invoiceValue = invoiceValue;
    }
}
