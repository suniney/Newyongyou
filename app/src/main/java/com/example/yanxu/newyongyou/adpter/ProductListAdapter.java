package com.example.yanxu.newyongyou.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.entity.HomeProductInfo;
import com.example.yanxu.newyongyou.listener.HomeClickedListener;
import com.example.yanxu.newyongyou.utils.DateUtil;

import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    List<HomeProductInfo> homeProductInfos;
    Context context;
    private HomeClickedListener mOnItemClickListener = null;
    public ProductListAdapter(Context context, List<HomeProductInfo> homeProductInfos,HomeClickedListener listener) {
        this.context = context;
        this.homeProductInfos = homeProductInfos;
        this.mOnItemClickListener=listener;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件

        return vh;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.product_name.setText(homeProductInfos.get(position).getName());
        viewHolder.product_cylcles.setText(context.getString(R.string.cylcles, homeProductInfos.get(position).getCylcles()));
        viewHolder.product_productClas.setText(homeProductInfos.get(position).getProductClas());
        viewHolder.product_interestRate.setText(context.getString(R.string.precent, homeProductInfos.get(position).getInterestRate()));
        viewHolder.product_startupMoney.setText(context.getString(R.string.money, homeProductInfos.get(position).getStartupMoney()));
        viewHolder.product_backInterest.setText(context.getString(R.string.precent, homeProductInfos.get(position).getBackInterest()));
        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(homeProductInfos.get(position).getId());
        viewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(homeProductInfos.get(position).getId().toString(), DateUtil.timeStamp2Date(homeProductInfos.get(position).getOpenDate(), null));
            }
        });
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return homeProductInfos.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name;
        public TextView product_cylcles;
        public TextView product_productClas;
        public TextView product_interestRate;
        public TextView product_startupMoney;
        public TextView product_backInterest;
        public RelativeLayout rl_main;

        public ViewHolder(View view) {
            super(view);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_cylcles = (TextView) view.findViewById(R.id.product_cylcles);
            product_productClas = (TextView) view.findViewById(R.id.product_productClas);
            product_interestRate = (TextView) view.findViewById(R.id.product_interestRate);
            product_startupMoney = (TextView) view.findViewById(R.id.product_startupMoney);
            product_backInterest = (TextView) view.findViewById(R.id.product_backInterest);
        }
    }


}
