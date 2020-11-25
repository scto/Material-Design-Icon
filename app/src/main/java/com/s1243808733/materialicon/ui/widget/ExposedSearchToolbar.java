package com.s1243808733.materialicon.ui.widget;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.s1243808733.materialicon.R;
import androidx.core.view.ViewCompat;

public class ExposedSearchToolbar extends Toolbar {

    private TextView mTitleTextView;

    public ExposedSearchToolbar(Context context) {
        super(context);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExposedSearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
		initTitle(context);
    }

    private void initTitle(Context context) {
        super.setTitle("");
        mTitleTextView = (TextView) View.inflate(context, R.layout.layout_searchbar_logo, null);
		/*Typeface typeface = ResourcesCompat.getFont(context, R.font.rockwell_std);
		if (typeface != null) {
			mTitleTextView.setTypeface(typeface);
		}*/
        addView(mTitleTextView);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        mTitleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }
}

