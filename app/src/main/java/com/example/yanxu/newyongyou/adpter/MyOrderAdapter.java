package com.example.yanxu.newyongyou.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.activity.BaseActivity;
import com.example.yanxu.newyongyou.entity.MyOrder;
import com.example.yanxu.newyongyou.listener.OrderClickedListener;
import com.example.yanxu.newyongyou.utils.DateUtil;

import java.util.List;

/**
 * Created by yanxu 2016/4/1.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {


    private Context mContext;

    private List<MyOrder> myOrder;

    public static OrderClickedListener mOrderClickedListener;

    public MyOrderAdapter(Context context, OrderClickedListener orderClickedListener, List<MyOrder> myOrder) {
        this.myOrder = myOrder;
        mContext = context;
        this.mOrderClickedListener = orderClickedListener;
    }

    @Override
    public int getItemCount() {
        return myOrder.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        switch (Integer.valueOf(myOrder.get(position).getStatus())) {
            case BaseActivity.STATUS_BOOK_UNSUCCESS:
                holder.tv_status.setText("预约失败");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.VISIBLE);
                holder.order_cancel.setVisibility(View.INVISIBLE);
                holder.order_upload_money.setVisibility(View.INVISIBLE);
                holder.order_upload_sign.setVisibility(View.INVISIBLE);
                holder.order_addrss.setVisibility(View.INVISIBLE);
                holder.order_reorder.setVisibility(View.VISIBLE);
                break;
            case BaseActivity.STATUS_OVER:
                holder.tv_status.setText("交易结束，并计入存量");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_BOOK_SUCCESS:
                holder.tv_status.setText("预约成功");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.VISIBLE);
                holder.order_cancel.setVisibility(View.VISIBLE);
                holder.order_upload_money.setVisibility(View.VISIBLE);
                holder.order_upload_sign.setVisibility(View.INVISIBLE);
                holder.order_addrss.setVisibility(View.VISIBLE);
                holder.order_reorder.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_BOOK_CHECK:
                holder.tv_status.setText("预约待审核");
                holder.rl_appont_1.setVisibility(View.VISIBLE);
                holder.rl_appont.setVisibility(View.GONE);
                break;
            case BaseActivity.STATUS_UPLOAD_CHECK:
                holder.tv_status.setText("打款凭证审核");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_UPLOAD_MONEY:
                holder.tv_status.setText("重新上传打款凭证");
                holder.rl_appont.setVisibility(View.VISIBLE);
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.order_cancel.setVisibility(View.INVISIBLE);
                holder.order_upload_money.setVisibility(View.VISIBLE);
                holder.order_upload_sign.setVisibility(View.INVISIBLE);
                holder.order_addrss.setVisibility(View.INVISIBLE);
                holder.order_reorder.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_FINISH:
                holder.tv_status.setText("客户完成交易");
                holder.rl_appont.setVisibility(View.VISIBLE);
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.order_cancel.setVisibility(View.INVISIBLE);
                holder.order_upload_money.setVisibility(View.INVISIBLE);
                holder.order_upload_sign.setVisibility(View.VISIBLE);
                holder.order_addrss.setVisibility(View.INVISIBLE);
                holder.order_reorder.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_REUPLOAD_SIGN:
                holder.tv_status.setText("重新上传合同");
                holder.rl_appont.setVisibility(View.VISIBLE);
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.order_cancel.setVisibility(View.INVISIBLE);
                holder.order_upload_money.setVisibility(View.INVISIBLE);
                holder.order_upload_sign.setVisibility(View.VISIBLE);
                holder.order_addrss.setVisibility(View.INVISIBLE);
                holder.order_reorder.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_UPLOAD_SIGN:
                holder.tv_status.setText("合同签署页审核");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_BACK_MONEY:
                holder.tv_status.setText("首次返佣");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_RECYCLE_SIGN:
                holder.tv_status.setText("回收合同原件");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            case BaseActivity.STATUS_CLOSE:
                holder.tv_status.setText("订单关闭");
                holder.rl_appont_1.setVisibility(View.GONE);
                holder.rl_appont.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        holder.order_name.setText(myOrder.get(position).getProductName());
        holder.order_date.setText(DateUtil.timeStamp2Date(String.valueOf(myOrder.get(position).getOrderDate()), null));
        holder.money.setText(mContext.getString(R.string.mymoney, myOrder.get(position).getMoney().intValue()));
        final String orderId = myOrder.get(position).getOrderId().toString();
        holder.order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onLayoutClick(orderId, myOrder.get(position).getAddrId().toString());
            }
        });
        holder.order_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onCancelClick(orderId);
            }
        });
        holder.order_cancel_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onCancelClick(orderId);
            }
        });
        holder.order_upload_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onSignClick(orderId);
            }
        });
        holder.order_upload_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onMoneyClick(orderId);
            }
        });
        holder.order_addrss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onAddress(myOrder.get(position).getAddrId());
            }
        });
        holder.order_addrss_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onAddress(myOrder.get(position).getAddrId());
            }
        });
        holder.order_reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderClickedListener.onOrderClick(myOrder.get(position).getProId().toString(),myOrder.get(position).getOrderId().toString(),myOrder.get(position).getAddrId().toString());
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.order_item, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView btn_Delete;
        public TextView textView;
        public ViewGroup layout_content;
        public TextView order_name;
        public TextView order_date;
        public TextView tv_status;
        public TextView money;
        public RelativeLayout rl_appont;
        public Button order_cancel;
        public Button order_upload_sign;
        public Button order_upload_money;
        public Button order_addrss;
        public Button order_reorder;
        public Button order_addrss_1;
        public Button order_cancel_1;
        public RelativeLayout rl_appont_1;
        //        public TextView order_type;
        public LinearLayout order_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            order_layout = (LinearLayout) itemView.findViewById(R.id.order_layout);
            order_name = (TextView) itemView.findViewById(R.id.order_name);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            order_date = (TextView) itemView.findViewById(R.id.order_date);
            money = (TextView) itemView.findViewById(R.id.money);
            rl_appont = (RelativeLayout) itemView.findViewById(R.id.rl_appont);
            rl_appont_1 = (RelativeLayout) itemView.findViewById(R.id.rl_appont_1);
            order_addrss = (Button) itemView.findViewById(R.id.order_addrss);
            order_cancel = (Button) itemView.findViewById(R.id.order_cancel);
            order_upload_sign = (Button) itemView.findViewById(R.id.order_upload_sign);
            order_upload_money = (Button) itemView.findViewById(R.id.order_upload_money);
            order_reorder = (Button) itemView.findViewById(R.id.order_reorder);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            textView = (TextView) itemView.findViewById(R.id.order_name);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            order_addrss_1 = (Button) itemView.findViewById(R.id.order_addrss_1);
            order_cancel_1 = (Button) itemView.findViewById(R.id.order_cancel_1);

        }
    }


    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }


}

