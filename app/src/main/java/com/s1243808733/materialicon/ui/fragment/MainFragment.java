package com.s1243808733.materialicon.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.data.IconSectionMetaData;
import com.s1243808733.materialicon.data.model.IconMetaData;
import com.s1243808733.materialicon.parse.IconPackageParser;
import com.s1243808733.materialicon.ui.activity.SearchIconActivity;
import com.s1243808733.materialicon.ui.adapter.IconFragmentAdapter;
import com.s1243808733.materialicon.ui.anim.SearchToolBarTransitioner;
import com.s1243808733.materialicon.ui.anim.SearchTransitioner;
import com.s1243808733.materialicon.ui.anim.SimpleTransitionListener;
import com.s1243808733.materialicon.ui.fragment.dialog.OpenSourceLicenseDialogFragment;
import com.s1243808733.materialicon.ui.view.viewholder.FragmentViewHolder;
import com.s1243808733.materialicon.ui.widget.ExposedSearchToolbar;
import java.util.Collections;
import java.util.List;
import net.lucode.hackware.magicindicator.MagicIndicator;
import org.view.annotation.MenuItemEvent;
import org.view.annotation.OptionsMenu;
import org.view.annotation.ViewInject;

@OptionsMenu(R.menu.options_fragment_main)
public class MainFragment extends IconFragment {

	public static final String TAG = MainFragment.class.getName();

	private MainFragmentViewHolder mFragmentViewHolder;

	private SearchController mSearchController;

	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		FrameLayout childContainer = view.findViewById(R.id.container);
		childContainer.addView(super.onCreateView(inflater, null, savedInstanceState));
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mFragmentViewHolder = new MainFragmentViewHolder(this, view);

		initToolBar: {
			ExposedSearchToolbar searchToolbar = mFragmentViewHolder.searchToolbar;
			activity.setSupportActionBar(searchToolbar);

			ActionBar actionBar = activity.getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			DrawerLayout drawer_layout = mController.viewHolder.drawer_layout;
			mDrawerToggle = new ActionBarDrawerToggle(activity, drawer_layout, searchToolbar, android.R.string.ok, android.R.string.ok);
			mDrawerToggle.syncState();
			drawer_layout.addDrawerListener(mDrawerToggle);

			mSearchController = new SearchController(this, mController.viewHolder.magic_indicator, mFragmentViewHolder.container, mFragmentViewHolder.exposedSearchToolbarBg);
			mFragmentViewHolder.searchToolbar.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						mFragmentViewHolder.app_bar_layout.setExpanded(true, true);
						mSearchController.transitionToSearch();
					}

				});

		}

	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mContentViewController.viewHolder.empty_view.loading().show();
        mSearchController.searchToolBarTransitioner.collapseView(new SimpleTransitionListener(){

                @Override 
                public void onTransitionEnd(Transition transition) {
                    loadData();
                }
            });
    }

	private void loadData() {
		new LoadDataTask().execute();
	}

	@MenuItemEvent(android.R.id.home)
	void toggleDrawer(MenuItem item) {
		mDrawerToggle.onOptionsItemSelected(item);
	}

    @MenuItemEvent(R.id.openSourceLicenseItem)
    void showOpenSourceLicenseDialog() {
        OpenSourceLicenseDialogFragment dialog = new OpenSourceLicenseDialogFragment();
        dialog.show(getChildFragmentManager());
    }

	@MenuItemEvent(R.id.exitItem)
	@Override
	public void finishActivity() {
		super.finishActivity();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == SearchIconActivity.RESULT_CODE_SEARCH_EXIT) {
			mSearchController.searchTransitioner.expandView();
		}
	}

    @Override
    public boolean isInjectMenu() {
        return true;
    }

	private class LoadDataTask extends AsyncTask<Void,Void,IconSectionMetaData> {

		@Override
		protected IconSectionMetaData doInBackground(Void[] args) {
			List<IconMetaData> metaDatas =  IconPackageParser.getInstance().getMetaDatas();
			Collections.sort(metaDatas);
			IconSectionMetaData data = new IconSectionMetaData(metaDatas);
			return data;
		}

		@Override
		protected void onPostExecute(IconSectionMetaData result) {
			super.onPostExecute(result);

			ContentController contentViewController = getContentViewController();
			IconFragmentAdapter iconFragmentPageAdapter = contentViewController.getIconFragmentPageAdapter();
			contentViewController.setIconSectionMetaData(result);
			String[] sections = result.getSections();

			for (int i = 0; i < sections.length; i++) {
				String section = sections[i];
				IconPageFragment fragment = new IconPageFragment();
				fragment.setFragmentName(section);

				Bundle args = fragment.getArgsOrCreate();
				args.putString(IconPageFragment.KEY_SECTION, section);
				fragment.setArguments(args);
				fragment.setIconSectionData(result);
				iconFragmentPageAdapter.add(fragment);
			}

			getDrawerViewController().run();
			mContentViewController.viewHolder.empty_view.setVisibility(View.GONE);
            mSearchController.searchToolBarTransitioner.expandView();

			iconFragmentPageAdapter.notifyDataSetChanged();

			handleIntent(activity.getIntent());
		}

		private void handleIntent(final Intent intent) {
			if (intent == null) return;
			String action = intent.getAction();
			if (action == null) return;
            if (action.startsWith("@")) {
                try {
                    action = getResources().getString(Integer.parseInt(action.substring(1)));
                } catch (Throwable e) {
                    LogUtils.e(e);
                    return;
                }
            }

			if (getString(R.string.action_shortcut_search).equals(action)) {
				new Handler().postDelayed(new Runnable(){

						@Override
						public void run() {
							intent.setAction(null);
							mSearchController.transitionToSearch();
						}
					}, 800);
			}

		}

	}

	private static class MainFragmentViewHolder extends FragmentViewHolder {

		@ViewInject(R.id.app_bar_layout)
		public AppBarLayout app_bar_layout;

        @ViewInject(R.id.exposedSearchToolbarBg)
        public ViewGroup exposedSearchToolbarBg;

		@ViewInject(R.id.exposedSearchToolbar)
		public ExposedSearchToolbar searchToolbar;

		@ViewInject(R.id.magic_indicator)
		public MagicIndicator magic_indicator;

		@ViewInject(R.id.container)
		public FrameLayout container;

		public MainFragmentViewHolder(Fragment fragment, View view) {
			super(fragment, view.findViewById(R.id.fragment_main));
		}
	}

	private class SearchController implements SearchTransitioner.OnTransitionEndListener {

		private final Fragment fragment;

		public final SearchTransitioner searchTransitioner;

		public final SearchToolBarTransitioner searchToolBarTransitioner;

		private static final int REQUEST_CODE_SEARCH = 0x1000;

		public SearchController(Fragment fragment,
								View tabLayout,
								ViewGroup activityContent,
								ViewGroup searchToolbar) {
			this.fragment = fragment;
			searchTransitioner = new SearchTransitioner(fragment.getActivity(), tabLayout, activityContent , searchToolbar);
			searchToolBarTransitioner = new SearchToolBarTransitioner(fragment.getActivity(), searchToolbar);
			searchTransitioner.setOnTransitionEndListener(this);
		}

		public void transitionToSearch() {
			searchTransitioner.collapseView();
		}

		public void gotoSearchActivity() {
			fragment.startActivityForResult(new Intent(fragment.getActivity(), SearchIconActivity.class), REQUEST_CODE_SEARCH);
			fragment.getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}

		@Override
		public void onTransitionEnd(SearchTransitioner transitioner) {
			gotoSearchActivity();
		}

	}

}
