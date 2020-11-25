package com.s1243808733.materialicon.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.ui.activity.base.BaseActivity;
import com.s1243808733.materialicon.ui.fragment.IconFragment;
import com.s1243808733.materialicon.ui.fragment.SearchIconFragment;
import com.s1243808733.materialicon.ui.widget.SearchBar;
import org.view.annotation.ContentView;
import org.view.annotation.ViewInject;

@ContentView(R.layout.activity_search_icon)
public class SearchIconActivity extends BaseActivity {

	public static final int RESULT_CODE_SEARCH_EXIT = 0x1000;

	private ActivityViewHolder mViewHolder;

	private SearchListener mSearchListener;

	private SearchIconFragment mSearchIconFragment;

	@Override
	protected void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setResult(RESULT_CODE_SEARCH_EXIT);
		mViewHolder = new ActivityViewHolder(this);
		mSearchListener = new SearchListener();
	}

	@Override
	protected void onInitializeToolBar(Bundle savedInstanceState) {
		super.onInitializeToolBar(savedInstanceState);
		final SearchBar searchBar = mViewHolder.searchBar;
		searchBar.setHint(getString(R.string.hint_search_icon));
		searchBar.setSearchListener(mSearchListener);
		setSupportActionBar(searchBar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		searchBar.postDelayed(new Runnable(){

				@Override
				public void run() {
					KeyboardUtils.showSoftInput(searchBar.getEditText());
				}
			}, 250);
	}

	@Override
	protected void onInitializeView(Bundle savedInstanceState) {
		super.onInitializeView(savedInstanceState);
		mViewHolder.app_bar_layout.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        FragmentManager fm = getSupportFragmentManager();
        if(hasFragmentCache(SearchIconFragment.TAG)){
            fm.beginTransaction().remove(findFragmentByTag(SearchIconFragment.TAG))
                .commitNow();
        }
        mSearchIconFragment = new SearchIconFragment();
        fm.beginTransaction()
            .replace(R.id.container, mSearchIconFragment, SearchIconFragment.TAG)
            .commit();       
        mSearchIconFragment.addOnSearchListener(mSearchListener);
	}

	private class SearchListener 
	implements SearchBar.SearchListener,SearchIconFragment.OnSearchListener {

		@Override
		public void search(String text) {
			if (mSearchIconFragment != null) {
				mSearchIconFragment.search(text);
			}
		}

		@Override
		public void onSearchStart(SearchIconFragment fragment, String s) {
		}

		@Override
		public void onSearchCancel(SearchIconFragment fragment, String key) {
		}

		@Override
		public void onSearchEnd(SearchIconFragment fragment, String key) {
			IconFragment.FragmrntController controller = fragment.getController();
			IconFragment.ContentController contentViewController = fragment.getContentViewController();
			if (contentViewController.getIconFragmentPageAdapter().getCount() == 0) {
				controller.viewHolder.magic_indicator.setVisibility(View.GONE);
				controller.viewHolder.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
				mViewHolder.app_bar_layout.setExpanded(true, true);
				mViewHolder.app_bar_layout.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
			} else {
				controller.viewHolder.magic_indicator.setVisibility(View.VISIBLE);
				controller.viewHolder.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				mViewHolder.app_bar_layout.setElevation(0);
			}
		}

	}

	@Override
	protected void onHomeMenuItemClick(MenuItem item) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (mSearchIconFragment != null && mSearchIconFragment.isAdded()
            && !mSearchIconFragment.isDetached()) {
            if (mSearchIconFragment.onBackPressed()) {
                return;
            }
        }

		super.onBackPressed();
		overridePendingTransition(0, 0);
	}

    @Override
    public boolean isInjectField() {
        return false;
    }

    @Override
    public boolean isInjectMethod() {
        return false;
    }

	private static class ActivityViewHolder {

		@ViewInject(R.id.app_bar_layout)
		public AppBarLayout app_bar_layout;

		@ViewInject(R.id.searchBar)
		public SearchBar searchBar;

		@ViewInject(R.id.container)
		public FrameLayout container;

		public ActivityViewHolder(BaseActivity activity) {
			activity.getViewInjector().inject(this, activity);
		}
	}

}
