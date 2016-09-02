package com.example.yanxu.newyongyou.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.entity.Address;
import com.example.yanxu.newyongyou.listener.AddressClickedListener;
import com.example.yanxu.newyongyou.listener.IonSlidingViewClickListener;
import com.example.yanxu.newyongyou.widget.SlidingButtonView;

import java.util.List;


public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private List<Address> mDatas;
    public static AddressClickedListener mAddressClickedListener;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu = null;
    private Context mContext;

    public AddressListAdapter(Context context, List<Address> mDatas, IonSlidingViewClickListener listner, AddressClickedListener mAddressClickedListener) {
        this.mContext = context;
        this.mDatas = mDatas;
        mIDeleteBtnClickListener = listner;
        this.mAddressClickedListener = mAddressClickedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_address_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.address_name.setText(mDatas.get(position).getRealName());
        viewHolder.address_phone.setText(mDatas.get(position).getMobile());
        viewHolder.tv_address.setText(mDatas.get(position).getAreaName() + mDatas.get(position).getAddr());
        if (0 == mDatas.get(position).getIsDef().intValue()) {
            viewHolder.address_default.setVisibility(View.VISIBLE);
        } else {
            viewHolder.address_default.setVisibility(View.GONE);
        }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(mDatas.get(position).getId());
        viewHolder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddressClickedListener.onAddressClick(mDatas.get(position).getId(), mDatas.get(position).getAreaName(),
                        mDatas.get(position).getAddr(), viewHolder.address_name.getText().toString(),
                        viewHolder.address_phone.getText().toString(),
                        mDatas.get(position).getIsDef().toString());
            }
        });
        //设置内容布局的宽为屏幕宽度
        viewHolder.layout_content.getLayoutParams().width = getScreenWidth(mContext);

        viewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = viewHolder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n, mDatas.get(position).getId());
                }

            }
        });
        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = viewHolder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n, mDatas.get(position).getId());
            }
        });
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView address_name;
        public LinearLayout address;
        public TextView tv_address;
        public ViewGroup layout_content;
        public TextView address_phone;
        public TextView address_default;
        public RelativeLayout btn_Delete;

        public ViewHolder(View view) {
            super(view);
            btn_Delete = (RelativeLayout) itemView.findViewById(R.id.rl_delete);
            address_name = (TextView) view.findViewById(R.id.address_name);
            address_phone = (TextView) view.findViewById(R.id.address_phone);
            address_default = (TextView) view.findViewById(R.id.address_default);
            address = (LinearLayout) view.findViewById(R.id.address);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            ((SlidingButtonView) itemView).setSlidingButtonListener(AddressListAdapter.this);
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int position) {
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);

    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        Log.i("asd", "mMenu为null");
        return false;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


}
