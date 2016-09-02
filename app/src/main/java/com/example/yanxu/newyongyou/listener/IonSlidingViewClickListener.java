package com.example.yanxu.newyongyou.listener;

import android.view.View;

/**
 * @author yanxu
 * @date 2016/4/22.
 */
public interface IonSlidingViewClickListener {
    void onItemClick(View view, int position, String id);

    void onDeleteBtnCilck(View view, int position, String id);
}
