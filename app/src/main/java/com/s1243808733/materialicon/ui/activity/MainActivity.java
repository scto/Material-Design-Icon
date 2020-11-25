package com.s1243808733.materialicon.ui.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.blankj.utilcode.util.ToastUtils;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.ui.activity.base.BaseActivity;
import com.s1243808733.materialicon.ui.fragment.MainFragment;
import org.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private MainFragment mMainFragment;

	@Override
	protected void onInitializeView(Bundle savedInstanceState) {
		super.onInitializeView(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        if(hasFragmentCache(MainFragment.TAG)){
            fm.beginTransaction().remove(findFragmentByTag(MainFragment.TAG))
            .commitNow();
        }
        mMainFragment = new MainFragment();
        fm.beginTransaction()
            .replace(R.id.container, mMainFragment, MainFragment.TAG)
            .commit();
    }


    private long firstBackTime;
    @Override
    public void onBackPressed() {
        if (mMainFragment != null && mMainFragment.isAdded()
            && !mMainFragment.isDetached()) {
            if (mMainFragment.onBackPressed()) {
                return;
            }
        }
        if (System.currentTimeMillis() - firstBackTime > 2000) {
            ToastUtils.showShort(R.string.message_press_again_to_exit);
            firstBackTime = System.currentTimeMillis();
            return;
        }
		ToastUtils.cancel();
        super.onBackPressed();
    }

    @Override
    public boolean isInjectField() {
        return false;
    }

    @Override
    public boolean isInjectMethod() {
        return false;
    }

    @Override
    public boolean isInjectMenu() {
        return false;
    }

}
