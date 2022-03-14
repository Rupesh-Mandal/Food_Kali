package com.softkali.foodkali.dashboard.model;

import java.time.LocalDateTime;

public class OrderModel {
    private Long id;

    private String orderId;

    private String deliverAddress;
    private String totalRate;
    private String productQuantity;

    private String productId;
    private String userId;
    private String sellerId;



    private String productName;
    private String productDescription;
    private String productRate;
    private String productDeliverCharge;
    private String productImageLink;
    private String productHotelname;
    private String productEmail;
    private String productPhoneNumber;
    private String userPhoneNumber;
    private String userName;


    private String status;
    private String statusMessage;

    private String createdTime;
    private String location;

    public OrderModel() {
    }

    public OrderModel(Long id, String orderId, String deliverAddress, String totalRate, String productQuantity, String productId, String userId, String sellerId, String productName, String productDescription, String productRate, String productDeliverCharge, String productImageLink, String productHotelname, String productEmail, String productPhoneNumber, String userPhoneNumber, String userName, String status, String statusMessage, String createdTime, String location) {
        this.id = id;
        this.orderId = orderId;
        this.deliverAddress = deliverAddress;
        this.totalRate = totalRate;
        this.productQuantity = productQuantity;
        this.productId = productId;
        this.userId = userId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productRate = productRate;
        this.productDeliverCharge = productDeliverCharge;
        this.productImageLink = productImageLink;
        this.productHotelname = productHotelname;
        this.productEmail = productEmail;
        this.productPhoneNumber = productPhoneNumber;
        this.userPhoneNumber = userPhoneNumber;
        this.userName = userName;
        this.status = status;
        this.statusMessage = statusMessage;
        this.createdTime = createdTime;
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
