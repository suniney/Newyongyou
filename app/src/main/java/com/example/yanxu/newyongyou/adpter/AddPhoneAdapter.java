package com.example.yanxu.newyongyou.adpter;

/**
 * Created by yanxu 2016/4/1.
 */

//public class AddPhoneAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder>  {
//    private Context mContext;
//    private List<String> phones;
//    private List<Integer> heights;
//    public static PhoneClickedListener mPhoneClickedListener;
//    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
//    public AddPhoneAdapter(Context context, List<String> phones, PhoneClickedListener phoneClickedListener) {
//        this.mContext = context;
//        this.phones = phones;
//        this.mPhoneClickedListener = phoneClickedListener;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.setting_add_phone_item, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        //将创建的View注册点击事件
////        view.setOnClickListener(this);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(NoticeListAdapter.ViewHolder holder, int position) {
//        View view = LayoutInflater.from(holder.getContext()).inflate(R.layout.setting_add_phone_item, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        //将创建的View注册点击事件
////        view.setOnClickListener(this);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        viewHolder.getBinding().phoneCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPhoneClickedListener.onPhoneClick(holder.getBinding().tvPhone.getText().toString());
//            }
//        });
//        viewHolder.notice_pmTitle.setText(productMessages.get(position).getPmTitle());
//
//        //将数据保存在itemView的Tag中，以便点击时进行获取
////        viewHolder.itemView.setTag(datas[position]);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemClickListener.onItemClick(v, (String) v.getTag());
//        }
//    }
//
//    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//
//    //获取数据的数量
//    @Override
//    public int getItemCount() {
//        return productMessages.size();
//    }
//
//    //自定义的ViewHolder，持有每个Item的的所有界面元素
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView tv_phone;
//        public ViewHolder(View view) {
//            super(view);
//            tv_phone = (TextView) view.findViewById(R.id.tv_phone);
//
//        }
//    }
//
//
//}
//
