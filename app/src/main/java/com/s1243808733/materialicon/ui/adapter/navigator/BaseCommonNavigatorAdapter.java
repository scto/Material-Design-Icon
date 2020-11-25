package com.s1243808733.materialicon.ui.adapter.navigator;
import android.view.View;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

public abstract class BaseCommonNavigatorAdapter extends CommonNavigatorAdapter implements INavigatorAdapter,OnPageTitleViewClickListener {

	protected INavigatorAdapter mINavigatorAdapter;

	protected OnPageTitleViewClickListener mOnPageTitleViewClickListener;

	public BaseCommonNavigatorAdapter(INavigatorAdapter i, OnPageTitleViewClickListener l) {
		this.mINavigatorAdapter = i;
		this.mOnPageTitleViewClickListener = l;
	}

	public BaseCommonNavigatorAdapter(INavigatorAdapter i) {
		this.mINavigatorAdapter = i;
	}

	public void setOnPageTitleViewClickListener(OnPageTitleViewClickListener l) {
		mOnPageTitleViewClickListener = l;
	}

	@Override
	public int getCount() {
		return mINavigatorAdapter == null ?0: mINavigatorAdapter.getCount();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mINavigatorAdapter == null ?"": mINavigatorAdapter.getPageTitle(position);
	}

	@Override
	public void onPageTitleViewClick(View view, int position) {
		if (mOnPageTitleViewClickListener != null) {
			mOnPageTitleViewClickListener.onPageTitleViewClick(view, position);
		}
	}

}
