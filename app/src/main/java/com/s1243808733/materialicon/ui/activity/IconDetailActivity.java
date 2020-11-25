package com.s1243808733.materialicon.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.common.util.BitmapUtils;
import com.s1243808733.materialicon.data.IconDetailData;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.ui.activity.base.BaseActivity;
import com.s1243808733.materialicon.ui.view.IconGridView;
import com.s1243808733.materialicon.ui.view.viewholder.ActivityViewHolder;
import com.s1243808733.materialicon.ui.widget.DialogButtonBarLayout;
import org.view.annotation.ContentView;
import org.view.annotation.Event;
import org.view.annotation.ViewInject;

@ContentView(R.layout.activity_icon_detail)
public class IconDetailActivity extends BaseActivity {

	public static final int RESULT_CODE_ICON_OUT = 0xa0;

    public static final int RESULT_CODE_ON_BACKGROUND = 0xa1;

    public static final String EXTRA_ICON_LIST_POSITION = "extra_icon_list_position";

    private MyViewHolder mViewHolder;

    private DialogViewHolder mDialogViewHolder;

	private IconDetailData mIconData;

    private Bundle mArgs;

	@Override
	protected void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        mViewHolder = new MyViewHolder(this);
        mDialogViewHolder = new DialogViewHolder(this);
        Intent intent = getIntent();
        mArgs = intent.getBundleExtra(AppConstants.EXTRA_ARGS);
        mIconData = mArgs.getParcelable(AppConstants.EXTRA_ICON_DETAIL_DATA);

        AppUtils.registerAppStatusChangedListener(new Utils.OnAppStatusChangedListener(){

                private boolean isHandled;

                @Override
                public void onForeground(Activity p1) {
                }

                @Override
                public void onBackground(Activity p1) {
                    if (isHandled) return;
                    isHandled = true;
                    setResult(RESULT_CODE_ON_BACKGROUND, getIntent());
                    finish();
                    overridePendingTransition(0, 0);
                }

            });

	}

	@Override
	protected void onInitializeStatusBar(Bundle savedInstanceState) {
		super.onInitializeStatusBar(savedInstanceState);
		BarUtils.setStatusBarLightMode(this, false);
	}

	@Override
	protected void onInitializeToolBar(Bundle savedInstanceState) {
		super.onInitializeToolBar(savedInstanceState);
        mDialogViewHolder.icon.setVisibility(View.GONE);
		mDialogViewHolder.alertTitle.setText(mIconData.getIconMetaData().getName());
        adjutBackgroundWidth();
	}

    private void adjutBackgroundWidth() {
        View dialogBackground = findViewById(R.id.dialogBackground);
        ViewGroup.LayoutParams lp = dialogBackground.getLayoutParams();
        if (lp == null) {
            lp  = new ViewGroup.LayoutParams(-1, -1);
        }
        Resources res = getResources();
        float width = res.getFraction(
            R.fraction.dialog_min_width, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenWidth());
        lp.width = (int) (width + 0.5f);
        dialogBackground.setLayoutParams(lp);
    }

	@Override
	protected void onInitializeView(Bundle savedInstanceState) {
		super.onInitializeView(savedInstanceState);
        mViewHolder.svgView.setImageBitmap(BitmapUtils.drawColorExtractAlpha(mIconData.getSvgWrap().createBitmap(getResources().getDimensionPixelSize(R.dimen.icon_grid_detail_view_size)), ContextCompat.getColor(this, R.color.icon_grid_item), true));

        mDialogViewHolder.button1.setText(R.string.export_icon);
        mDialogViewHolder.button2.setText(R.string.cancel);
        mDialogViewHolder.buttonBarLayout.removeView(mDialogViewHolder.button3);
    }

    @Event(value = R.id.svg_view,values = {R.id.icon_layout})
    void toggleIconGridVisibility() {
        if (mViewHolder.iconGrid.getVisibility() == View.VISIBLE) {
            mViewHolder.iconGrid.setVisibility(View.INVISIBLE);
        } else {
            mViewHolder.iconGrid.setVisibility(View.VISIBLE);
        }
    }

	@Event(value = android.R.id.button1)
	void outIcon() {
		setResult(RESULT_CODE_ICON_OUT, getIntent());
		onBackPressed();
	}

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAfterTransition();
    }

	@Event(value = android.R.id.button2, values={R.id.root})
    @Override
    public void finishAfterTransition() {
        super.finishAfterTransition();
		overridePendingTransition(0, 0);
    }

    @Override
    public boolean isInjectField() {
        return false;
    }

    @Override
    public boolean isInjectMenu() {
        return false;
    }

    private class MyViewHolder extends ActivityViewHolder {

        @ViewInject(R.id.icon_grid)
        public IconGridView iconGrid;

        @ViewInject(R.id.svg_view)
        public ImageView svgView;

        public MyViewHolder(Activity activity) {
            super(activity);
        }

    }

    private class DialogViewHolder extends ActivityViewHolder {

        @ViewInject(android.R.id.icon)
        public ImageView icon;

        @ViewInject(R.id.alertTitle)
        public TextView alertTitle;

        @ViewInject(R.id.buttonBarLayout)
        public DialogButtonBarLayout buttonBarLayout;

        @ViewInject(android.R.id.button1)
        public Button button1;

        @ViewInject(android.R.id.button2)
        public Button button2;

        @ViewInject(android.R.id.button3)
        public Button button3;

        public DialogViewHolder(Activity activity) {
            super(activity);
        }

    }

}
