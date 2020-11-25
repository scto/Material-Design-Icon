package com.s1243808733.materialicon.ui.adapter;
import androidx.fragment.app.FragmentManager;
import com.s1243808733.materialicon.ui.adapter.base.BaseFragmentPagerAdapter;
import com.s1243808733.materialicon.ui.adapter.navigator.INavigatorAdapter;
import com.s1243808733.materialicon.ui.fragment.IconPageFragment;
import java.util.ArrayList;
import java.util.List;

public class IconFragmentAdapter extends BaseFragmentPagerAdapter implements INavigatorAdapter {

    private List<IconPageFragment> fragments = new ArrayList<>();
	
	public final FragmentManager fm;

    public IconFragmentAdapter(FragmentManager fm) {
        super(fm);
		this.fm = fm;
    }

	public void setFragments(List<IconPageFragment> fragments) {
		this.fragments = fragments;
	}

	public List<IconPageFragment> getFragments() {
		return fragments;
	}

    public IconFragmentAdapter add(IconPageFragment fragment) {
        fragments.add(fragment);
        return this;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public IconPageFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
		IconPageFragment fragment = getItem(position);
        return fragment.getFragmentName();
    }
	
}
