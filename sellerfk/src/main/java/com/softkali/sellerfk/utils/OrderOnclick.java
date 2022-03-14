package com.softkali.sellerfk.utils;


import com.softkali.sellerfk.dashboard.model.OrderModel;

public interface OrderOnclick {
    void onDeliverd(OrderModel orderModel);
    void onDeliverdFaild(OrderModel orderModel);
    void onCancel(OrderModel orderModel,String massege);
    void onAccept(OrderModel orderModel);

}
