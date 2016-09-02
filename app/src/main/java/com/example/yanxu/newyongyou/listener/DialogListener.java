package com.example.yanxu.newyongyou.listener;

import org.json.JSONObject;

public interface DialogListener {

        void onApply(JSONObject obj, String clazz);
        void onCancel();
}