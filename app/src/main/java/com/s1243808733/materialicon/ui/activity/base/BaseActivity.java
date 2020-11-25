package com.s1243808733.materialicon.ui.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.s1243808733.materialicon.R;
import org.view.InjectOptions;
import org.view.ViewInjector;
import org.view.ViewManager;
import org.view.annotation.MenuItemEvent;
import org.view.manager.MenuManager;

public abstract class BaseActivity extends AppCompatActivity {

	private ViewInjector mViewInjector;

    private MenuManager mMenuManager;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToastUtils.getDefaultMaker().setMode(getResources().getBoolean(R.bool.night_mode)
                                             ?ToastUtils.MODE.DARK: ToastUtils.MODE.LIGHT);
        onInitializeStatusBar(savedInstanceState);
		if (isInjectView()) onInjectActivity(getViewInjector(), savedInstanceState);
        onActivityCreated(savedInstanceState);
		onInitializeToolBar(savedInstanceState);
		onInitializeView(savedInstanceState);
    }

    protected void onActivityCreated(Bundle savedInstanceState) {
	}

	protected void onInitializeStatusBar(Bundle savedInstanceState) {
		BarUtils.setStatusBarLightMode(this, true);
    }

	protected void onInitializeToolBar(Bundle savedInstanceState) {
	}

    protected void onInitializeView(Bundle savedInstanceState) {
    }

	protected void onInjectActivity(ViewInjector viewInjector, Bundle savedInstanceState) {
        InjectOptions options =  InjectOptions.newInstance();
        options.setInjectContentView(isInjectContentView());
        options.setInjectField(isInjectField());
        options.setInjectMethod(isInjectMethod());
        mViewInjector.inject(this, options);
	}

    public ViewInjector getViewInjector() {
        return mViewInjector == null ? (mViewInjector = ViewManager.view()) : mViewInjector;
    }

    public MenuManager getMenuManager() {
        return mMenuManager;
    }

    public boolean isInjectView() {
        return true;
    }

    public boolean isInjectContentView() {
        return true;
    }

    public boolean isInjectField() {
        return true;
    }

    public boolean isInjectMethod() {
        return true;
    }

    public boolean isInjectMenu() {
        return true;
    }

	public boolean hasFragment(String tag) {
		return findFragmentByTag(tag) != null;
	}

    public boolean hasFragmentCache(String tag) {
        return findFragmentByTag(tag) != null;
    }

	public <T extends Fragment>T findFragmentByTag(String tag) {
		return (T)getSupportFragmentManager().findFragmentByTag(tag);
	}

    @MenuItemEvent(android.R.id.home)
    protected void onHomeMenuItemClick(MenuItem item) {
		finish();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    public <T extends View> T findViewById(int target, int id) {
        return findViewById(target).findViewById(id);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isInjectMenu()) {
            mMenuManager = mViewInjector.inject((Activity)this, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mMenuManager != null && !mMenuManager.isBinded(item)) {
            mMenuManager.onMenuItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}

