package com.s1243808733.materialicon.ui.view.helper;

import androidx.viewpager.widget.ViewPager;
import net.lucode.hackware.magicindicator.MagicIndicator;

public class ViewPagerHelper {
    
    public static void bind(final MagicIndicator magicIndicator, ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
				}

				@Override
				public void onPageSelected(int position) {
					magicIndicator.onPageSelected(position);
				}

				@Override
				public void onPageScrollStateChanged(int state) {
					magicIndicator.onPageScrollStateChanged(state);
				}
			});
    }
    
}
