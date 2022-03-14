package com.softkali.foodkali.utils;

public class Constant {
    public final static String BaseUrl="http://foodkali.com/api/v1";

//    public final static String BaseUrl="http://192.168.1.67:8080/api/v1";

    public final static String getAllLocation=BaseUrl+"/location/get_all";

    public final static String UserSignin=BaseUrl+"/auth/sign_in_user";
    public final static String UserSignUp=BaseUrl+"/auth/sign_up_user";

    public final static String getAllProductUrl=BaseUrl+"/product/get_all_product_user";

    public final static String addOrder=BaseUrl+"/order/add";
    public final static String getAllOrder=BaseUrl+"/order/get_order_as_usser";

}
