package com.s1243808733.materialicon.ui.view.viewholder;

import android.view.View;
import androidx.fragment.app.Fragment;
import org.view.ViewInjector;
import org.view.ViewManager;

public abstract class FragmentViewHolder {

    public final Fragment fragment;

    private final ViewInjector mViewInjector;

    public final View fragmentView;

    public FragmentViewHolder(Fragment fragment) {
        this(fragment, fragment.getView());
    }

    public FragmentViewHolder(Fragment fragment, View view) {
        this.fragment = fragment;
        this.mViewInjector = ViewManager.view();
        this.fragmentView = view;
        inject(mViewInjector, view);
    }

    protected void inject(ViewInjector viewInjector, View view) {
        viewInjector.inject(this, view);
    }

}
