package com.s1243808733.materialicon.ui.adapter.navigator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.s1243808733.materialicon.common.util.ThemeUtils;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import androidx.core.graphics.TypefaceCompat;

public class WrapNavigatorAdapter extends BaseCommonNavigatorAdapter {

	public WrapNavigatorAdapter(INavigatorAdapter l) {
		super(l);
	}

	public WrapNavigatorAdapter(INavigatorAdapter i, OnPageTitleViewClickListener l) {
		super(i, l);
	}

    @SuppressWarnings("WrongConstant")
	@Override
	public IPagerTitleView getTitleView(Context context, final int index) {
		ClipPagerTitleView pagerTitleView = new ClipPagerTitleView(context);
		CharSequence title = getPageTitle(index);
		pagerTitleView.setText(title == null ?"": title.toString());
		pagerTitleView.setTextSize(SizeUtils.dp2px(14f));
		pagerTitleView.setTextColor(ThemeUtils.getTextColorTertiary(context));
		pagerTitleView.setClipColor(ThemeUtils.getColorAccent(context));
		pagerTitleView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onPageTitleViewClick(v, index);
				}
			});
        try {
            pagerTitleView.getPaint().setTypeface(Typeface.create("sans-serif-medium", 0));
        } catch (Throwable e) {}
		return pagerTitleView;
	}

	@Override
	public IPagerIndicator getIndicator(Context context) {
		WrapPagerIndicator indicator = new WrapPagerIndicator(context);
		indicator.setFillColor(ColorUtils.setAlphaComponent(ThemeUtils.getTextColorTertiary(context), 0.1f));
		return indicator;
	}
}
