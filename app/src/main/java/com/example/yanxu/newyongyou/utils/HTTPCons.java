package com.example.yanxu.newyongyou.utils;

/**
 * Created by yd on 2016/3/16.
 */
public interface HTTPCons {
    String NET_CHANGCE_ACTION = "com.youyong.netchange";
    long period = 180000L;
    long step = 1000L;
    String webhost = "http://121.43.118.86:10220";
    //    String host = "http://api.mlmsalon.com/api";
    String host = "http://121.40.92.117:8777/api";
//        String host = "http://192.168.1.88:8080/api";
//    String get_banner_action = host + "/home/getBanners";
    String get_home_action = host + "/home/home";
    String post_login_action = host + "/login/login";
    String post_regist_action = host + "/login/regist";
    String add_address_action = host + "/myInfo/addPostAddr";
    String deleteAddr_action = host + "/myInfo/deleteAddr";
    String turnBank_action = host + "/myInfo/turnBank";
    String myInfo_action = host + "/myInfo/myInfo";
    String userCenter_action = host + "/myInfo/userCenter";
    String myAddr_action = host + "/myInfo/myAddr";
    String updatePostAddr_action = host + "/myInfo/updatePostAddr";
    String searchAddr_action = host + "/myInfo/searchAddr";
    String balancePage_action = host + "/myInfo/balancePage";
    String withdrawPage_action = host + "/myInfo/withdrawPage";
    String add_saveOrganization_action = host + "/myInfo/saveOrganization";
    String get_notice_action = host + "/myInfo/bbs";
    String get_normalSettings = host + "/myInfo/normalSettings";
    String addBankCard_action = host + "/myInfo/addBankCard";
    String updatePwd_action = host + "/myInfo/updatePwd";
    String chooseMobile_action = host + "/myInfo/chooseMobile";
    String mobileList_action = host + "/myInfo/mobileList";
    String uploadHeadPic_action = host + "/myInfo/uploadHeadPic";
    String orderDetail_action = host + "/order/orderDetail";
    String saveOrderDetail_action = host + "/order/saveOrderDetail";
    String orderNum_action = host + "/order/orderNum";
    String uploadPaymentPic_action = host + "/order/uploadPaymentPic";
    String uploadSignPic_action = host + "/order/uploadSignPic";
    String cancelOrder_action = host + "/order/cancelOrder";
    String searchAnnouncement_action = host + "/home/searchAnnouncement";
    String searchMessage_action = host + "/home/searchMessage";
    String withdraw_action = host + "/myInfo/withdraw";
    String modifyName_action = host + "/myInfo/modifyName";
    String turnOrderDetail_action = host + "/order/turnOrderDetail";
    String share_action = host + "/home/share";
    String sendVerifyCode_action = host + "/login/sendVerifyCode";
    String resetPwd_action = host + "/login/resetPwd";
    String readMessage_action = host + "/home/readMessage";
    String myAsset_action = host + "/myAsset/myAsset";
    String userCenterShare_action = host + "/home/userCenterShare";


}
