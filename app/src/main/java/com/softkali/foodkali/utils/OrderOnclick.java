package com.softkali.foodkali.utils;

import com.softkali.foodkali.dashboard.model.OrderModel;

public interface OrderOnclick {
    void onReceived(OrderModel orderModel);
    void onCancel(OrderModel orderModel);

}
