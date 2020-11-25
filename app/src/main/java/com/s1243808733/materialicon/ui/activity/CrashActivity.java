package com.s1243808733.materialicon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.ui.activity.base.BaseActivity;
import org.view.annotation.ContentView;
import org.view.annotation.MenuItemEvent;
import org.view.annotation.OptionsMenu;
import org.view.annotation.ViewInject;

@ContentView(R.layout.activity_crash)
@OptionsMenu(R.menu.options_crash)
public class CrashActivity extends BaseActivity {

	@ViewInject(R.id.error_view)
	private TextView mErrorView;

    private String mCrashInfo;
    
	@Override
	protected void onInitializeView(Bundle savedInstanceState) {
		super.onInitializeView(savedInstanceState);
		mCrashInfo = getIntent().getStringExtra("crashInfo");
		mErrorView.setText(mCrashInfo);
	}

	@MenuItemEvent(R.id.copyItem) 
	void copyCrashLog() {
		ClipboardUtils.copyText(mCrashInfo);
		ToastUtils.showShort(R.string.message_copied_to_clipboard);
	}

	@MenuItemEvent(R.id.restartItem) 
	@Override
	public void onBackPressed() {
		Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
    
}
