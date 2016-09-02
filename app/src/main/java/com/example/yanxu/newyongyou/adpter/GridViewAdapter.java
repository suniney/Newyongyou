package com.example.yanxu.newyongyou.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.listener.OnDeleteClickedListener;

import java.util.List;
import java.util.Map;

/**
 * @author yanxu
 * @date 2016/4/25.
 */
public class GridViewAdapter extends BaseAdapter {
    private String names[];

    private List icons;
    private Context mContext;
    private TextView text;
    private ImageView img;
    private LinearLayout grid_layout;
    private ImageView photo;
    private View deleteView;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    OnDeleteClickedListener listener;
    private boolean isCheck;

    public GridViewAdapter(Context mContext, List icons, OnDeleteClickedListener listener) {
        this.mContext = mContext;
        this.icons = icons;
        this.listener = listener;
    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return icons.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(
                R.layout.pic_item, null);
        Map map = (Map) icons.get(position);
        grid_layout = (LinearLayout) convertView.findViewById(R.id.grid_item);
        photo = (ImageView) convertView.findViewById(R.id.photo);
        isCheck = (boolean) map.get("isCheck");
        grid_layout.setClickable(isCheck);
        if (isCheck) {
            grid_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, "1", position);
                }
            });
        }
        deleteView = convertView.findViewById(R.id.delete_markView);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(v, "1", position);
            }
        });
        if (map.get("photo") instanceof Bitmap) {
            photo.setImageBitmap((Bitmap) map.get("photo"));
        }
        isShowDelete = (boolean) map.get("isShow");
        deleteView.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);//设置删除按钮是否显示
        return convertView;
    }
}




