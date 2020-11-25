package com.s1243808733.materialicon.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.data.IconDatas;
import com.s1243808733.materialicon.data.IconSectionMetaData;
import com.s1243808733.materialicon.data.SVGWrapper;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconSection;
import com.s1243808733.materialicon.ui.adapter.IconFragmentAdapter;
import com.s1243808733.materialicon.ui.adapter.base.BaseFragmentPagerAdapter;
import com.s1243808733.materialicon.ui.adapter.navigator.INavigatorAdapter;
import com.s1243808733.materialicon.ui.adapter.navigator.OnPageTitleViewClickListener;
import com.s1243808733.materialicon.ui.adapter.navigator.WrapNavigatorAdapter;
import com.s1243808733.materialicon.ui.fragment.base.Backable;
import com.s1243808733.materialicon.ui.fragment.base.BaseFragment;
import com.s1243808733.materialicon.ui.view.helper.ViewPagerHelper;
import com.s1243808733.materialicon.ui.view.viewholder.FragmentViewHolder;
import com.s1243808733.materialicon.ui.view.viewholder.ViewHolder;
import com.santalu.emptyview.EmptyView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import org.view.annotation.ContentView;
import org.view.annotation.ViewInject;

@ContentView(R.layout.fragment_icon)
public class IconFragment extends BaseFragment implements Backable {

	public static final String TAG = "IconFragment";

	protected FragmrntController mController;

	protected ContentController mContentViewController;

	protected DrawerController mDrawerViewController;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mController = new FragmrntController(view.findViewById(R.id.fragment_icon), savedInstanceState);
		mContentViewController =  new ContentController(mController.viewHolder.drawer_layout, savedInstanceState);
		mDrawerViewController = new DrawerController(mController.viewHolder.drawer_layout, savedInstanceState);
		for (IController controller : new IController[]{
			mController,
			mContentViewController,
			mDrawerViewController
		}) {
			controller.onCreated(savedInstanceState);
		}
        
	}

	public FragmrntController getController() {
		return mController;
	}

	public ContentController getContentViewController() {
		return mContentViewController;
	}

	public DrawerController getDrawerViewController() {
		return mDrawerViewController;
	}

	@Override
	public boolean onBackPressed() {
		if (mDrawerViewController != null && mDrawerViewController.isDrawerOpen()) {
			mDrawerViewController.closeDrawer();
			return true;
		}
		return false;
	}

    @Override
    public boolean isInjectField() {
        return false;
    }

    @Override
    public boolean isInjectMethod() {
        return false;
    }

    @Override
    public boolean isInjectMenu() {
        return false;
    }
    
	public static interface IController {
		public abstract void onCreated(Bundle savedInstanceState);
	}

	public class FragmrntController implements IController, OnPageTitleViewClickListener {

		public final MainFragmentViewHolder viewHolder;

		public FragmrntController(View view, Bundle savedInstanceState) {
			viewHolder = new MainFragmentViewHolder(IconFragment.this, view);
        }

		@Override
		public void onCreated(Bundle savedInstanceState) {
			initMagicIndicator();
		}

		private void initMagicIndicator() {
			MagicIndicator indicator = viewHolder.magic_indicator;
			CommonNavigator nav = new CommonNavigator(activity);
			nav.setScrollPivotX(0.35f);
			nav.setSkimOver(true);
			nav.setLeftPadding(SizeUtils.dp2px(16));
			nav.setRightPadding(nav.getLeftPadding());
			nav.setAdapter(new WrapNavigatorAdapter(new INavigatorAdapter() {
										 @Override public int getCount() {
											 return mContentViewController.iconFragmentPageAdapter.getCount();
										 }
										 @Override public CharSequence getPageTitle(int position) {
											 return mContentViewController.iconFragmentPageAdapter.getPageTitle(position).toString().toUpperCase(Locale.ENGLISH);
										 }
									 }, this));

			indicator.setNavigator(nav);
			ViewPagerHelper.bind(viewHolder.magic_indicator, mContentViewController.viewHolder.view_pager);
		}

		@Override
		public void onPageTitleViewClick(View view, int position) {
			mDrawerViewController.closeDrawer();
			mContentViewController.viewHolder.view_pager.setCurrentItem(position);
		}

	}

	public class ContentController implements IController,BaseFragmentPagerAdapter.OnDataSetChangedListener {

		public final ContentViewHolder viewHolder;

		private IconFragmentAdapter iconFragmentPageAdapter;

		private IconSectionMetaData iconSectionMetaData;

		public ContentController(View view, Bundle savedInstanceState) {
			viewHolder = new ContentViewHolder(view);
			IconFragmentAdapter adapter = createIconFragmentPageAdapter();
			setIconFragmentPageAdapter(adapter);
		}

        @Override
        public void onCreated(Bundle savedInstanceState) {
		}

		public void setIconFragmentPageAdapter(IconFragmentAdapter adapter) {
			this.iconFragmentPageAdapter = adapter;
			viewHolder.view_pager.setAdapter(adapter);
		}

		public IconFragmentAdapter createIconFragmentPageAdapter() {
			IconFragmentAdapter adapter = new IconFragmentAdapter(getChildFragmentManager());
			adapter.addOnDataSetChangedListener(this);
			return adapter;
		}

		public IconFragmentAdapter getIconFragmentPageAdapter() {
			return iconFragmentPageAdapter;
		}

		public void setIconSectionMetaData(IconSectionMetaData iconSectionMetaData) {
			this.iconSectionMetaData = iconSectionMetaData;
		}

		public IconSectionMetaData getIconSectionMetaData() {
			return iconSectionMetaData;
		}

		public void setCurrentPageItem(int position) {
			viewHolder.view_pager.setCurrentItem(position);
		}

		public int getCurrentPageItemPos() {
			return  viewHolder.view_pager.getCurrentItem();
		}

		@Override
		public void onDataSetChanged(BaseFragmentPagerAdapter adapter) {
			mController.viewHolder.magic_indicator.getNavigator().notifyDataSetChanged();
		}

	}

	public class DrawerController implements IController,Runnable {

		public final int SECTION_GROUPID = 0xffcc;;

		public final DrawerViewHolder viewHolder;

		public DrawerController(View view, Bundle savedInstanceState) {
			this.viewHolder = new DrawerViewHolder(view);
		}

        @Override
        public void onCreated(Bundle savedInstanceState) {
        }

		public void setNavCheckedItem(int id) {
			Menu menu = viewHolder.nav_view.getMenu();
			for (int i = 0; i < menu.size(); i++) {
				MenuItem item = menu.getItem(id);
				if (item.getGroupId() == SECTION_GROUPID && item.getItemId() == id) {
					viewHolder.nav_view.setCheckedItem(id);
					return;
				}
			}
		}

		@Override
		public void run() {
			Menu menu = viewHolder.nav_view.getMenu();
			menu.clear();

			LinkedHashMap<String, IconDatas> iconSectionMap = mContentViewController.iconSectionMetaData.getIconSectionMap();
			String[] sections = mContentViewController.iconSectionMetaData.getSections();
			for (int i = 0; sections != null && i < sections.length; i++) {
				String tag = sections[i];
				IconDatas iconDatas = iconSectionMap.get(tag);
				List<IconInfo> infos = iconDatas.getIconInfos();
				String name = String.format("%s (%d)", tag, infos.size());

				Drawable icon = null;
				findFirstIcon: {
					for (IconSection section : infos) {
						if (section instanceof IconInfo) {
							IconInfo info = (IconInfo) section;
							SVGWrapper wrap = info.getSvgWrap();
							try {
								icon = new BitmapDrawable(ImageUtils.drawable2Bitmap(new PictureDrawable(wrap.createPicture(96))));
							} catch (Throwable ignored) {}
							break findFirstIcon;
						}
					}
				}

				int itemId = i;
				MenuItem menuItem = menu.add(SECTION_GROUPID, itemId, 0, name);
				menuItem.setIcon(icon);
				menuItem.setCheckable(true);
				if (i == mContentViewController.viewHolder.view_pager.getCurrentItem()) {
					menuItem.setChecked(true);
				}

				final int pagePos = i;
				menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {
							closeDrawer();
							new Handler().postDelayed(new Runnable(){

									@Override
									public void run() {
										mContentViewController.setCurrentPageItem(pagePos);
									}
								}, 300);
							return false;
						}
					});

			}

			mContentViewController.viewHolder.view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

					@Override
					public void onPageScrolled(int p1, float p2, int p3) {
					}

					@Override
					public void onPageSelected(int position) {
						setNavCheckedItem(position);
					}

					@Override
					public void onPageScrollStateChanged(int p1) {
					}

				});
		}

		public void closeDrawer() {
			if (isDrawerOpen()) {
				mController.viewHolder.drawer_layout.closeDrawer(Gravity.LEFT);
			}
		}

		public void openDrawer() {
			if (!isDrawerOpen()) {
				mController.viewHolder.drawer_layout.openDrawer(Gravity.LEFT);
			}
		}

		public boolean isDrawerOpen() {
			return mController.viewHolder.drawer_layout.isDrawerOpen(Gravity.LEFT);
		}

	}

	public static class MainFragmentViewHolder extends FragmentViewHolder {

		@ViewInject(R.id.magic_indicator)
		public MagicIndicator magic_indicator;

		@ViewInject(R.id.drawer_layout)
		public DrawerLayout drawer_layout;

		public MainFragmentViewHolder(Fragment fragment, View view) {
			super(fragment, view);
		}

	}

	public static class ContentViewHolder extends ViewHolder {

		@ViewInject(R.id.view_pager)
		public ViewPager view_pager;

		@ViewInject(R.id.empty_view)
		public EmptyView empty_view;

		public ContentViewHolder(View view) {
			super(view.findViewById(R.id.content));
		}

	}

	public static class DrawerViewHolder extends ViewHolder {

		@ViewInject(R.id.nav_view)
		public NavigationView nav_view;

		public DrawerViewHolder(View view) {
			super(view.findViewById(R.id.drawer_start_view));
		}

	}

}
