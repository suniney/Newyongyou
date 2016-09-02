package com.example.yanxu.newyongyou.adpter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.yanxu.newyongyou.fragment.BannerFragment;

import java.util.List;


public class BannerFragmentAdapter extends FragmentStatePagerAdapter {

	private List<BannerFragment> mContents;

	public BannerFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public BannerFragmentAdapter(FragmentManager fm, List<BannerFragment> contents) {
		super(fm);
		mContents = contents;
	}

	@Override
	public BannerFragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mContents.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContents.size();
	}

}
