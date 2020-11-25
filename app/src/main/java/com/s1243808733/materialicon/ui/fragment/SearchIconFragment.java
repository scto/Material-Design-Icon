package com.s1243808733.materialicon.ui.fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.data.IconSectionMetaData;
import com.s1243808733.materialicon.data.model.IconMetaData;
import com.s1243808733.materialicon.parse.IconPackageParser;
import com.s1243808733.materialicon.ui.adapter.IconFragmentAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class SearchIconFragment extends IconFragment {

	public static final String TAG = SearchIconFragment.class.getName();

	private static final int STATE_SEARCHING = 0x1;

	private static final int STATE_CANCELED = 0x2;

	private static final int STATE_END = 0x2;

	private final List<OnSearchListener> mOnSearchListener = new ArrayList<>();

	private SearchTask mSearchTask;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getController().viewHolder.magic_indicator.setVisibility(View.GONE);
		getController().viewHolder.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

    public void search(String s) {
		if (mSearchTask != null && mSearchTask.isSearching()) {
			mSearchTask.cancel();
		}
        if (!isAdded() || isDetached()) return;
		mSearchTask = new SearchTask(s, new OnSearchListener(){

				@Override
				public void onSearchStart(SearchIconFragment fragment, String s) {
					mContentViewController.viewHolder.empty_view.setVisibility(View.VISIBLE);
					mContentViewController.viewHolder.empty_view.loading()
						.show();

					for (OnSearchListener l : mOnSearchListener) {
						l.onSearchStart(fragment, s);
					}
				}

				@Override
				public void onSearchCancel(SearchIconFragment fragment, String key) {
					for (OnSearchListener l : mOnSearchListener) {
						l.onSearchCancel(fragment, key);
					}
				}

				@Override
				public void onSearchEnd(SearchIconFragment fragment, String key) {
					if (!TextUtils.isEmpty(key) && fragment.getContentViewController().getIconFragmentPageAdapter().getCount() == 0) {
						getContentViewController().viewHolder.empty_view.empty()
                            .setEmptyDrawable(R.drawable.ic_empty_downasaur)
                            .setEmptyTitle(getString(R.string.message_search_no_page).concat("😏"))
                            .show();
					} else {
						getContentViewController().viewHolder.empty_view.setVisibility(View.GONE);
					}

					for (OnSearchListener l : mOnSearchListener) {
						l.onSearchEnd(fragment, key);
					}
				}
			});
		mSearchTask.onSearchListener.onSearchStart(this, s);
		mSearchTask.execute(s);
	}

	public void addOnSearchListener(OnSearchListener listener) {
		mOnSearchListener.add(listener);
	}

	public void removeOnSearchListener(OnSearchListener listener) {
		mOnSearchListener.remove(listener);
	}

	@Override
	public void onDestroyView() {
		if (mSearchTask != null && mSearchTask.isSearching()) {
			mSearchTask.cancel();
		}
		super.onDestroyView();
	}

	private class SearchTask extends AsyncTask<String,Void,IconSectionMetaData> {

		private int state;

		private final String key;

		private final OnSearchListener onSearchListener;

		public SearchTask(String key, OnSearchListener onSearchListener) {
			this.key = key;
			this.onSearchListener = onSearchListener;
		}

		public boolean cancel() {
			if (cancel(true)) {
				state = STATE_CANCELED;
				onSearchListener.onSearchEnd(SearchIconFragment.this, key);
				return true;
			}
			return false;
		}

		public boolean isSearching() {
			return mSearchTask.state == STATE_SEARCHING;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			state = STATE_SEARCHING;
		}

		private List<IconMetaData> mSearchSourceData;
		@Override
		protected IconSectionMetaData doInBackground(String[] args) {
			List<IconMetaData> filterDatas =  new ArrayList<>();

			filter: {
				if (key.length() == 0) {
					break filter;
				}
				String keyUp = key.toUpperCase(Locale.ENGLISH);
				int keyLen = key.length();

				if (mSearchSourceData == null) {
					mSearchSourceData = IconPackageParser.getInstance().getNewMetaDatas();
				}

				for (IconMetaData icon : mSearchSourceData) {
					TreeSet<String> aliases = new TreeSet<>();
					aliases.add(icon.getName());
					aliases.addAll(icon.getAliases());

					filterAliase: {
						for (String aliase : aliases) {
							String aliaseUp = aliase.toUpperCase(Locale.ENGLISH);
							int startIndex = aliaseUp.indexOf(keyUp);
							if (startIndex >= 0) {
								//int endIndex = startIndex + keyLen;
								filterDatas.add(icon);
								break filterAliase;
							}
						}
					}

				}
				Collections.sort(filterDatas);
			}

			IconSectionMetaData data = new IconSectionMetaData(filterDatas);
			return data;
		}

		@Override
		protected void onPostExecute(IconSectionMetaData result) {
			super.onPostExecute(result);
			state = STATE_END;

			ContentController contentViewController = getContentViewController();
			contentViewController.setIconSectionMetaData(result);

			IconFragmentAdapter adapter = contentViewController.createIconFragmentPageAdapter();

			String[] sections = result.getSections();

			List<IconPageFragment> fragments = new ArrayList<>();
			for (int i = 0; i < sections.length; i++) {
				String section = sections[i];
				IconPageFragment fragment = new IconPageFragment();
				fragment.setFragmentName(section);

				Bundle args = fragment.getArgsOrCreate();
				args.putString(IconPageFragment.KEY_SECTION, section);
				fragment.setArguments(args);

				fragment.setIconSectionData(result);
				fragments.add(fragment);

			}

			adapter.setFragments(fragments);
			contentViewController.setIconFragmentPageAdapter(adapter);
			contentViewController.viewHolder.view_pager.setAdapter(adapter);
			contentViewController.onDataSetChanged(adapter);
			getController().viewHolder.magic_indicator.getNavigator().onPageSelected(contentViewController.viewHolder.view_pager.getCurrentItem());

			getDrawerViewController().run();
			onSearchListener.onSearchEnd(SearchIconFragment.this, key);
		}

	}

	public interface OnSearchListener {

		void onSearchStart(SearchIconFragment fragment, String key);

		void onSearchCancel(SearchIconFragment fragment, String key);

		void onSearchEnd(SearchIconFragment fragment, String key);

	}

}
