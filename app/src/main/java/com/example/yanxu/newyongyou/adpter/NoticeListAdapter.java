package com.example.yanxu.newyongyou.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.entity.ProductMessage;
import com.example.yanxu.newyongyou.listener.NoticeClickedListener;
import com.example.yanxu.newyongyou.utils.DateUtil;

import java.util.ArrayList;


public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder>  {
    private ArrayList<ProductMessage> productMessages;
    private NoticeClickedListener mOnItemClickListener = null;
    public NoticeListAdapter(ArrayList<ProductMessage> productMessages, NoticeClickedListener mOnItemClickListener) {
        this.productMessages = productMessages;
        this.mOnItemClickListener = mOnItemClickListener;
    }



    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_notice_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {

        viewHolder.notice_pmTitle.setText(productMessages.get(position).getPmTitle());
        viewHolder.notice_pmDate.setText(DateUtil.timeStamp2Date(productMessages.get(position).getPmDate().toString(), "yyyy-MM-dd"));
        if (productMessages.get(position).getPmDesc()!=null) {
            viewHolder.notice_pmContent.setText(productMessages.get(position).getPmDesc());
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(productMessages.get(position).getId());
        viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onNoticeClick(productMessages.get(position).getId());
            }
        });
    }



    //获取数据的数量
    @Override
    public int getItemCount() {
        return productMessages.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notice_pmTitle;
        public TextView notice_pmDate;
        public TextView notice_pmContent;
public LinearLayout ll_main;
        public ViewHolder(View view) {
            super(view);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            notice_pmTitle = (TextView) view.findViewById(R.id.notice_pmTitle);
            notice_pmDate = (TextView) view.findViewById(R.id.notice_pmDate);
            notice_pmContent = (TextView) view.findViewById(R.id.notice_pmContent);

        }
    }

}
