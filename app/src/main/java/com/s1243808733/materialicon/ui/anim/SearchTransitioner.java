package com.s1243808733.materialicon.ui.anim;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

import android.app.Activity;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.ui.anim.FadeInTransition;

public class SearchTransitioner {
    private Activity mActivity;
    private ViewGroup mSearchToolbar;
    private View mTabLayout;
    private ViewGroup mActivityContent;

    private int mToolbarMargin;
    private boolean mTransitioning;

	private OnTransitionEndListener mOnTransitionEndListener;

    public SearchTransitioner(Activity activity,
                              View tabLayout,
                              ViewGroup activityContent,
                              ViewGroup searchToolbar
							  ) {
        mActivity = activity;
        mTabLayout = tabLayout;
        mActivityContent = activityContent;
        mSearchToolbar = searchToolbar;
        mToolbarMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_tight);
    }

	public void setOnTransitionEndListener(OnTransitionEndListener onTransitionEndListener) {
		this.mOnTransitionEndListener = onTransitionEndListener;
	}

    public void collapseView() {
        if (mTransitioning) {
            return;
        }
        Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());
        TransitionManager.beginDelayedTransition(mSearchToolbar, transition);
        expandToolbar();
        ViewFader.hideContentOf(mSearchToolbar);
        mTabLayout.animate().translationYBy(-mTabLayout.getHeight()).setDuration(250).start();
        mActivityContent.animate().alpha(0).setDuration(250).start();
    }

    private void expandToolbar() {
		FrameLayout.LayoutParams layoutParams =
			(FrameLayout.LayoutParams) mSearchToolbar.getLayoutParams();
		layoutParams.setMargins(0, 0, 0, 0);
		mSearchToolbar.setLayoutParams(layoutParams);
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mTransitioning = true;
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mTransitioning = false;
				if (mOnTransitionEndListener != null) {
					mOnTransitionEndListener.onTransitionEnd(SearchTransitioner.this);
				}
            }
        };
    }

    public void expandView() {

        TransitionManager
			.beginDelayedTransition(mSearchToolbar, FadeInTransition.createTransition());
        FrameLayout.LayoutParams layoutParams =
			(FrameLayout.LayoutParams) mSearchToolbar.getLayoutParams();
        layoutParams.setMargins(mToolbarMargin, mToolbarMargin, mToolbarMargin, mToolbarMargin);
        ViewFader.showContent(mSearchToolbar);
        mSearchToolbar.setLayoutParams(layoutParams);
        mTabLayout.animate().translationY(0).setDuration(250).start();
        mActivityContent.animate().alpha(1).setDuration(250).start();

    }

	public interface OnTransitionEndListener {
		void onTransitionEnd(SearchTransitioner transitioner);
	}

}
