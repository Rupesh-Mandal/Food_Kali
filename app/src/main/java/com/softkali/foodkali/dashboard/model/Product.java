package com.softkali.foodkali.dashboard.model;


import com.softkali.foodkali.utils.ProductType;

public class Product {

    private Long id;
    private String productId;
    private String sellerId;

    private String productName;
    private String productDescription;
    private String productRate;
    private String productDeliverCharge;
    private String productImageLink;
    private String productHotelname;
    private String productEmail;
    private String productPhoneNumber;
    private String createdTime;
    private ProductType productType;
    private String location;

    public Product() {
    }

    public Product(Long id, String productId, String sellerId, String productName, String productDescription, String productRate, String productDeliverCharge, String productImageLink, String productHotelname, String productEmail, String productPhoneNumber, String createdTime, ProductType productType, String location) {
        this.id = id;
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productRate = productRate;
        this.productDeliverCharge = productDeliverCharge;
        this.productImageLink = productImageLink;
        this.productHotelname = productHotelname;
        this.productEmail = productEmail;
        this.productPhoneNumber = productPhoneNumber;
        this.createdTime = createdTime;
        this.productType = productType;
        this.location = location;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public String getProductDeliverCharge() {
        return productDeliverCharge;
    }

    public void setProductDeliverCharge(String productDeliverCharge) {
        this.productDeliverCharge = productDeliverCharge;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public String getProductHotelname() {
        return productHotelname;
    }

    public void setProductHotelname(String productHotelname) {
        this.productHotelname = productHotelname;
    }

    public String getProductEmail() {
        return productEmail;
    }

    public void setProductEmail(String productEmail) {
        this.productEmail = productEmail;
    }

    public String getProductPhoneNumber() {
        return productPhoneNumber;
    }

    public void setProductPhoneNumber(String productPhoneNumber) {
        this.productPhoneNumber = productPhoneNumber;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
