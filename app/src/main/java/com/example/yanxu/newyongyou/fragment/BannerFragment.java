package com.example.yanxu.newyongyou.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yanxu.newyongyou.R;
import com.example.yanxu.newyongyou.listener.OnBannerClickedListener;
import com.example.yanxu.newyongyou.utils.HTTPCons;


@SuppressLint("ValidFragment")
public class BannerFragment extends Fragment implements HTTPCons {
    private Context context;
    private OnBannerClickedListener listener;
    ImageView iv;
    Bundle b;

    public BannerFragment() {
    }

    public BannerFragment(Context con, OnBannerClickedListener listener) {
        this.context = con;
        this.listener = listener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.banner_view, container, false);
//        setRetainInstance(true);//设置true，表明在存储中保留fragment对象。
        iv = (ImageView) view.findViewById(R.id.iv_banner);
        b = getArguments();
//        MyOkHttpUtils.getImage(b.getString("bannerUrl"), new ImageCallback() {
//            @Override
//            public void onResponse(Bitmap response,int id) {
//                iv.setImageBitmap(response);
//            }
//        });
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                listener.onItemClick(arg0, b.getString("actUrl"));
            }
        });

        return view;
    }

}
