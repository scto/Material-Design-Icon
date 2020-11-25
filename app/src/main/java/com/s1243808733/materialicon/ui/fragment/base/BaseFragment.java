package com.s1243808733.materialicon.ui.fragment.base;

import android.app.Application;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.s1243808733.materialicon.ui.activity.base.BaseActivity;
import org.view.InjectOptions;
import org.view.ViewInjector;
import org.view.ViewManager;
import org.view.manager.MenuManager;

public abstract class BaseFragment extends Fragment {

    private boolean mInjected = false;

    private ViewInjector mViewInjector;

    private MenuManager mMenuManager;

    public BaseActivity activity;

	private String fragmentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			fragmentName = savedInstanceState.getString("fragmentName");
		}
        activity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isInjectView()) {
            mInjected = true;
            InjectOptions options = InjectOptions.newInstance();
            options.setInjectField(isInjectField());
            options.setInjectMethod(isInjectMethod());
            return getViewInjector().inject(this, inflater, container, options);
        } else {
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isInjectView() && !mInjected) {
            getViewInjector().inject(this, this.getView());
        }
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getFragmentName() != null) {
			outState.putString("fragmentName", getFragmentName());
		}
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public String getFragmentName() {
		return fragmentName;
	}

    public ViewInjector getViewInjector() {
        if (mViewInjector == null) {
            mViewInjector = ViewManager.view();
        }
        return mViewInjector;
    }

    public MenuManager getMenuManager() {
        return mMenuManager;
    }

    public boolean isInjectView() {
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

    public Application getApplication() {
        return getActivity().getApplication();
    }

	public void finishActivity() {
		activity.finish();
	}

	public Bundle getArgsOrCreate() {
		if (getArguments() == null) {
			setArguments(new Bundle());
		}
		return getArguments();
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isInjectMenu()) {
            mMenuManager = mViewInjector.inject(this, menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mMenuManager != null && isInjectMenu() && !mMenuManager.isBinded(item)) {
            mMenuManager.onMenuItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

}
