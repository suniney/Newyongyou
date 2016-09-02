package com.example.yanxu.newyongyou.utils;


import com.example.yanxu.newyongyou.listener.NameValuePair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * post加密处理
 */
public class MsgDigestUtil {
    public static String key = "3e9bb86c6980c3b79e5b936ce10b9b96";

    public static String getDigestParam(Map<String, String> strMap) {

        String res = "";
        for (String n : strMap.keySet()) {
            res += n + "=" + strMap.get(n) + "&";
        }

        if (res != null && (res.length() - 1) == res.lastIndexOf("&")) {
//			res=res.substring(0, res.length()-1);
            res += "key=" + key;
        }
        return sha256Hex(res);
    }
    private static class KeyComparator implements
            Comparator<Map.Entry<String, String>> {
        public int compare(Map.Entry<String, String> m,
                           Map.Entry<String, String> n) {
            return m.getKey().compareTo(n.getKey());
        }
    }
    public static String getDigestParam(List<NameValuePair> strMap) {

        String res = "";
        for (NameValuePair n : strMap) {
            res += n.getName() + "=" + n.getValue() + "&";
        }

        if (res != null && (res.length() - 1) == res.lastIndexOf("&")) {
//			res=res.substring(0, res.length()-1);
            res += "key=" + key;
        }
        return sha256Hex(res);
    }

    public static String sha256Hex(String param) {
        try {
            MessageDigest alg = MessageDigest.getInstance("SHA-256");
            alg.update(param.getBytes());
            byte[] digestr = alg.digest();
            return byte2hex(digestr);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }


        return null;

    }

    public static String byte2hex(byte[] digestr) {
        return String.format("%0" + (digestr.length * 2) + "X", new BigInteger(1, digestr)).toLowerCase();
    }


}
