package com.example.yanxu.newyongyou.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.entity.Notice;
import com.example.yanxu.newyongyou.utils.DateUtil;

import java.util.ArrayList;

public class MessageNoticeListAdapter extends RecyclerView.Adapter<MessageNoticeListAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<Notice> notice;

    public MessageNoticeListAdapter(ArrayList<Notice> notice) {
        this.notice = notice;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;



    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_notice_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tv_notice_title.setText(notice.get(position).getTitle());
        viewHolder.tv_notice_msg.setText(notice.get(position).getContent());
        viewHolder.tv_notice_date.setText(DateUtil.timeStamp2Date(notice.get(position).getCreated().toString(),null));
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(notice.get(position).getId());
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return notice.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_notice_title;
        public TextView tv_notice_msg;
        public TextView tv_notice_date;

        public ViewHolder(View view) {
            super(view);
            tv_notice_title = (TextView) view.findViewById(R.id.tv_notice_title);
            tv_notice_msg = (TextView) view.findViewById(R.id.tv_notice_msg);
            tv_notice_date = (TextView) view.findViewById(R.id.tv_notice_date);

        }
    }


}
