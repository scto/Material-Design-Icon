package com.s1243808733.materialicon.ui.anim;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

import android.app.Activity;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.ui.anim.FadeInTransition;

public class SearchToolBarTransitioner {
    private Activity mActivity;
    private ViewGroup mSearchToolbar;

    private int mToolbarMargin;
    private boolean mTransitioning;

    public SearchToolBarTransitioner(Activity activity,
									 ViewGroup searchToolbar
									 ) {
        mActivity = activity;
        mSearchToolbar = searchToolbar;
        mToolbarMargin = activity.getResources().getDimensionPixelSize(R.dimen.padding_tight);
    }

    public void collapseView(final SimpleTransitionListener l) {
        if (mTransitioning) {
            return;
        }
        Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone(l));
        transition.setDuration(0);
        TransitionManager.beginDelayedTransition(mSearchToolbar, transition);
        expandToolbar();
        ViewFader.hideContentOf(mSearchToolbar);

    }

    private void expandToolbar() {
		FrameLayout.LayoutParams layoutParams =
			(FrameLayout.LayoutParams) mSearchToolbar.getLayoutParams();
		layoutParams.setMargins(0, 0, 0, 0);
		mSearchToolbar.setLayoutParams(layoutParams);

    }

    private Transition.TransitionListener navigateToSearchWhenDone(final SimpleTransitionListener l) {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mTransitioning = true;
                if (l != null) l.onTransitionStart(transition);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mTransitioning = false;
                if (l != null) l.onTransitionEnd(transition);
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

    }

}
