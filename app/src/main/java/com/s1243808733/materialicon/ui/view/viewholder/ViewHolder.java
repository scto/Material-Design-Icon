package com.s1243808733.materialicon.ui.view.viewholder;

import android.view.View;
import org.view.ViewInjector;
import org.view.ViewManager;

public abstract class ViewHolder {

    public final View view;
    
    private final ViewInjector mViewInjector;

    public ViewHolder(View view) {
        this.view = view;
        this.mViewInjector = ViewManager.view();
        if (isInject()) {
            inject(mViewInjector, view);
        }
    }

    public ViewInjector getViewInjector() {
        return mViewInjector;
    }

    public void inject(ViewInjector viewInjector, View view) {
        viewInjector.inject(this, view);
    }

    public boolean isInject() {
        return true;
    }

}
