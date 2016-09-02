package com.example.yanxu.newyongyou.listener;

import android.view.View;

public interface OnDeleteClickedListener {
        void onDeleteClick(View v, String url, int position);
        void onItemClick(View v, String url, int position);
}