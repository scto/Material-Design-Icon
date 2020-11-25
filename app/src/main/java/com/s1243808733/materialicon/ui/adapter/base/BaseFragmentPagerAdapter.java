package com.s1243808733.materialicon.ui.adapter.base;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.FragmentStatePagerAdapter;

public abstract class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter{
    
	private List<OnDataSetChangedListener> mOnDataSetChangedListeners= new ArrayList<>();
	
    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
	
	public void addOnDataSetChangedListener(OnDataSetChangedListener l) {
		this.mOnDataSetChangedListeners.add(l);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		notifyListenerDataSetChanged();
	}

	protected void notifyListenerDataSetChanged() {
		for(OnDataSetChangedListener l : mOnDataSetChangedListeners){
			l.onDataSetChanged(this);
		}
	}
	
	public interface OnDataSetChangedListener {
		void onDataSetChanged(BaseFragmentPagerAdapter adapter);
	}
	
}
