package com.softkali.sellerfk.utils;

public class Constant {
    public final static String BaseUrl="http://foodkali.com/api/v1";


//    public final static String BaseUrl="http://192.168.1.67:8080/api/v1";

    public final static String getAllLocation=BaseUrl+"/location/get_all";

    public final static String sellerSignin=BaseUrl+"/auth/sign_in_seller";
    public final static String sellerSignUp=BaseUrl+"/auth/sign_up_seller";

    public final static String addProductUrl=BaseUrl+"/product/add_product";
    public final static String updateProductUrl=BaseUrl+"/product/update_product";
    public final static String getAllProductUrl=BaseUrl+"/product/get_all_product_seller";
    public final static String deletProductUrl=BaseUrl+"/product/delet_product";

    public final static String getAllOrder=BaseUrl+"/order/get_order_as_seller";
    public final static String acceptOrder=BaseUrl+"/order/accept_order";
    public final static String cancelOrder=BaseUrl+"/order/cancel_order_by_seller";
    public final static String deliverdFaildOrder=BaseUrl+"/order/faild_to_delivered_order";
    public final static String deliverdOrder=BaseUrl+"/order/delivered_order";

}
