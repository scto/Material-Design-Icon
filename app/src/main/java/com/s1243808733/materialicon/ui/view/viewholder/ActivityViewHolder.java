package com.s1243808733.materialicon.ui.view.viewholder;

import android.app.Activity;
import android.view.View;
import org.view.ViewInjector;
import org.view.ViewManager;

public abstract class ActivityViewHolder {

    public final Activity activity;

    public final View view;

    private final ViewInjector mViewInjector;

    public ActivityViewHolder(Activity activity) {
        this(activity, null);
    }

    public ActivityViewHolder(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        this.mViewInjector = ViewManager.view();

        if (view == null) {
            inject(mViewInjector, activity);
        } else {
            inject(mViewInjector, activity, view);
        }

    }

    protected void inject(ViewInjector viewInjector, Activity activity) {
        mViewInjector.inject(this, activity);
    }

    protected void inject(ViewInjector viewInjector, Activity activity, View view) {
        mViewInjector.inject(this, view);
    }

}
