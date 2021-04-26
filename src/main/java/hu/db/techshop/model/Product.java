package hu.db.techshop.model;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

public class Product {

    private int id;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private int categoryId;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String productNumber;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String productName;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String slug;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String shortDescription;

    private String description;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private int price;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private double vat;

    private boolean active;
    private Timestamp createdAt;

    public Product() {
    }

    public Product(int categoryId, String productNumber, String productName, String slug,
                   String shortDescription, String description, int price, double vat, boolean active) {
        this.categoryId = categoryId;
        this.productNumber = productNumber;
        this.productName = productName;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.vat = vat;
        this.active = active;
    }

    public Product(int id, int categoryId, String productNumber, String productName,
                   String slug, String shortDescription, String description, int price,
                   double vat, boolean active, Timestamp createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.productNumber = productNumber;
        this.productName = productName;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.vat = vat;
        this.active = active;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
