package com.s1243808733.materialicon.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.common.util.KeyText;
import com.s1243808733.materialicon.data.IconDetailData;
import com.s1243808733.materialicon.data.IconSectionMetaData;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconSection;
import com.s1243808733.materialicon.ui.activity.IconDetailActivity;
import com.s1243808733.materialicon.ui.adapter.IconAdapter;
import com.s1243808733.materialicon.ui.fragment.base.BaseFragment;
import com.s1243808733.materialicon.ui.fragment.dialog.IconGenerateDialogFragment;
import com.s1243808733.materialicon.ui.fragment.dialog.ImageGenerateDialogFragment;
import com.s1243808733.materialicon.ui.fragment.dialog.SvgGenerateDialogFragment;
import com.s1243808733.materialicon.ui.fragment.dialog.VectorGenerateDialogFragment;
import com.s1243808733.materialicon.ui.view.decoration.GridSectionAverageGapItemDecoration;
import com.s1243808733.materialicon.ui.view.viewholder.ViewHolder;
import java.util.ArrayList;
import java.util.List;
import org.view.annotation.ContentView;
import org.view.annotation.ViewInject;

@ContentView(R.layout.fragment_page_icon)
public class IconPageFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {

	public static final String TAG = "IconPageFragment";

    public static final String KEY_SECTION = "section";

    public static final String KEY_OUT_TO_IMAGE = "out_to_image";

    public static final String KEY_OUT_TO_VECTOR = "out_to_vector";

    public static final String KEY_OUT_TO_SVG = "out_to_svg";

    private FragmentManager childFM;

    private Argument mArgument;

    private IconSectionMetaData mIconSectionData;

    private ContentViewHolder mContentViewHolder;

    private GridLayoutManager mLayoutManager;

    private IconAdapter mIconAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childFM =  getChildFragmentManager();
        mArgument = new Argument(getArguments());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);  
        mContentViewHolder = new ContentViewHolder(view);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
		mContentViewHolder.view.setVisibility(View.INVISIBLE);

        RecyclerView recycler_view = mContentViewHolder.recycler_view;
        mLayoutManager = new GridLayoutManager(getActivity(), calcNumOfRows());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addItemDecoration(new GridSectionAverageGapItemDecoration(0, 0, 10, 10));

        mIconAdapter = new IconAdapter(getActivity());
        mIconAdapter.setOnItemClickListener(this);
        mIconAdapter.bindToRecyclerView(recycler_view);
        mIconAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recycler_view.setAdapter(mIconAdapter);

        mIconAdapter.setSectionData(mIconSectionData);
        mIconAdapter.setSection(mArgument.getSection());

		recycler_view.post(new Runnable(){

				@Override
				public void run() {
					if (isRemoving() || !isAdded() || isDetached())return;
					mContentViewHolder.view.setVisibility(View.VISIBLE);
					DisplayMetrics dm = getResources().getDisplayMetrics();
					final int h_screen = dm.heightPixels;
					mContentViewHolder.view.setTranslationY(h_screen / 1.5f);
					mContentViewHolder.view.setAlpha(0);
					mContentViewHolder.view.animate()
						.alpha(1)
						.translationY(0)
						.setInterpolator(new AccelerateDecelerateInterpolator())
						.setDuration(350)
						.start();
				}
			});
	}

    private int calcNumOfRows() {
        int width = ScreenUtils.getScreenWidth();
        float iconSize = getResources().getDimension(R.dimen.icon_grid_item_size);
        int spanCount = (int) (width / iconSize);
        return spanCount;
    }

    public void setIconSectionData(IconSectionMetaData iconSectionData) {
        this.mIconSectionData = iconSectionData;
        if (mIconAdapter != null) {
            mIconAdapter.setSectionData(iconSectionData);
        }
    }

    public IconSectionMetaData getIconSectionData() {
        return mIconSectionData;
    }

    public void setAdapterSection(String section) {
        if (mIconAdapter != null) {
            mIconAdapter.setSection(section);
        }
    }

	public String getSection() {
		return mArgument.getSection();
	}

    private long lastItemClickTime;
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (System.currentTimeMillis() - lastItemClickTime < 500) {
            return;
        }
        lastItemClickTime = System.currentTimeMillis();

        IconSection convertItem = (IconSection) adapter.getItem(position);
        if (convertItem instanceof IconInfo) {
            IconInfo iconInfo = (IconInfo) convertItem;
            showIconDetailDialog(iconInfo, position, ScreenUtils.isPortrait());
        }
    }

    private void showIconDetailDialog(IconInfo iconInfo, int position, boolean anim) {
        if (getContext() == null)return;

        Intent intent = new Intent(getContext(), IconDetailActivity.class);

        Bundle args = new Bundle();
        IconDetailData data = new IconDetailData(iconInfo.getIconMetaData(), iconInfo.getSvgData());
        args.putParcelable(AppConstants.EXTRA_ICON_DETAIL_DATA, data);
        intent.putExtra(AppConstants.EXTRA_ARGS, args);

        intent.putExtra(IconDetailActivity.EXTRA_ICON_LIST_POSITION, position);

        ImageView svg_view = (ImageView)mIconAdapter.getViewByPosition(position, R.id.svg_view);
        if (svg_view == null || !anim) {
            startActivityForResult(intent, 1);
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(), svg_view, svg_view.getTransitionName()).toBundle();
            startActivityForResult(intent, 1, bundle);
        }
    }

    private StatusChangedListener mStatusChangedListener;
	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == IconDetailActivity.RESULT_CODE_ICON_OUT) {
			showIconExportDialog(data.getBundleExtra(AppConstants.EXTRA_ARGS));
		} else if (resultCode == IconDetailActivity.RESULT_CODE_ON_BACKGROUND) {
            final int position = data.getIntExtra(IconDetailActivity.EXTRA_ICON_LIST_POSITION, -1);
            final IconSection item = mIconAdapter.getItem(position);
            if (item != null && item instanceof IconInfo) {
                IconInfo iconInfo = (IconInfo) item;
                if (mStatusChangedListener == null) {
                    mStatusChangedListener = new StatusChangedListener(iconInfo, position);
                    AppUtils.registerAppStatusChangedListener(mStatusChangedListener);
                } else {
                    mStatusChangedListener.enable = true;
                    mStatusChangedListener.iconInfo = iconInfo;
                    mStatusChangedListener.position = position;
                }
            }
        }

    }

    private void showIconExportDialog(Bundle args) {
        final IconDetailData data = args.getParcelable(AppConstants.EXTRA_ICON_DETAIL_DATA);

        List<CharSequence> itemList = new ArrayList<>();
        itemList.add(new KeyText(KEY_OUT_TO_IMAGE, getString(R.string.export_to_image)));
        itemList.add(new KeyText(KEY_OUT_TO_VECTOR, getString(R.string.export_to_vector)));
        itemList.add(new KeyText(KEY_OUT_TO_SVG, getString(R.string.export_to_svg)));

        final CharSequence[] items = itemList.toArray(new CharSequence[]{});
        AlertDialog dialog = new AlertDialog.Builder(activity)
            .setTitle(getString(R.string.export_type))
            .setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    CharSequence text = items[which];
                    if (text instanceof KeyText) {
                        handleKey(((KeyText)text).getKey());
                    }
                }

                private void handleKey(String key) {
                    switch (key) {
                        case KEY_OUT_TO_IMAGE:
                            showImageGenerateDialog(data);
                            break;
                        case KEY_OUT_TO_VECTOR:
                            showVectorGenerateDialogDialog(data);
                            break;
                        case KEY_OUT_TO_SVG:
                            showSvgGenerateDialogDialog(data);
                            break;
                    }
                }

            })
            .setNegativeButton(R.string.cancel, null)
            .create();
        dialog.show();
    }

    private void showImageGenerateDialog(IconDetailData data) {
        ImageGenerateDialogFragment fragment = IconGenerateDialogFragment.newInstance(
            ImageGenerateDialogFragment.class, data);
        fragment.show(childFM, ImageGenerateDialogFragment.TAG);
    }

    private void showVectorGenerateDialogDialog(IconDetailData data) {
        VectorGenerateDialogFragment fragment = IconGenerateDialogFragment.newInstance(
            VectorGenerateDialogFragment.class, data);
        fragment.show(childFM, VectorGenerateDialogFragment.TAG);
    }

    private void showSvgGenerateDialogDialog(IconDetailData data) {
        SvgGenerateDialogFragment fragment = IconGenerateDialogFragment.newInstance(
            SvgGenerateDialogFragment.class, data);
        fragment.show(childFM, SvgGenerateDialogFragment.TAG);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mLayoutManager != null && mIconAdapter != null) {
            mLayoutManager.setSpanCount(calcNumOfRows());
            mIconAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private class StatusChangedListener implements Utils.OnAppStatusChangedListener {

        public boolean enable = true;

        public IconInfo iconInfo;

        public int position;

        public StatusChangedListener(IconInfo iconInfo, int position) {
            this.iconInfo = iconInfo;
            this.position = position;
        }

        @Override
        public void onForeground(Activity p1) {
            if (!enable) return;
            enable = false;
            new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        if (!isAdded()) return;
                        showIconDetailDialog(iconInfo, position, ScreenUtils.isPortrait());
                    }
                }, 250);
        }

        @Override
        public void onBackground(Activity p1) {

        }
    }

    private class Argument {

        public final String section;

        public Argument(Bundle args) {
            section = args.getString(KEY_SECTION);
        }

		public String getSection() {
			return section;
		}

    }

    private static class ContentViewHolder extends ViewHolder {

        @ViewInject(R.id.recycler_view)
        public RecyclerView recycler_view;

        public ContentViewHolder(View view) {
            super(view);
        }

    }

}
