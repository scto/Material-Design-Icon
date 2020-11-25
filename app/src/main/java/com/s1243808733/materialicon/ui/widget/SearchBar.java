package com.s1243808733.materialicon.ui.widget;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.s1243808733.materialicon.R;
import org.view.ViewManager;
import org.view.annotation.ViewInject;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/5
 */

public class SearchBar extends Toolbar {
    public SearchBar(Context context) {
        super(context);
        init();
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchBar(Context context,
                     @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @ViewInject(R.id.editText)
    AppCompatEditText mEditText;

    @ViewInject(R.id.imageView)
    ImageView mActionIcon;

    @ViewInject(R.id.indicatorView)
    ProgressBar mIndicatorView;

	public EditText getEditText() {
		return mEditText;
	}

    private void init() {

        View view = View.inflate(getContext(), R.layout.layout_search_bar, null);

        addView(view);
		ViewManager.view().inject(this);

        addTintedUpNavigation();
		mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						InputMethodManager imm = (InputMethodManager) getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
						mEditText.requestFocus();
						return true;
					}
					return false;
				}
			});

        mEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (s != null && s.length() > 0) {
						mActionIcon.setVisibility(VISIBLE);
					} else {
						mActionIcon.setVisibility(GONE);
					}
					if (mSearchListener != null) {
						mSearchListener.search(s == null ? "" : s.toString());
					}
				}
			});

        mActionIcon.setVisibility(GONE);

        mActionIcon.setImageResource(R.drawable.ic_search_close);
		mActionIcon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View view) {
					mEditText.setText("");
					if(!KeyboardUtils.isSoftInputVisible((Activity)getContext())){
						mEditText.post(new Runnable(){

								@Override
								public void run() {
									KeyboardUtils.showSoftInput(mEditText);
									
								}
							});
					}
				}
			});
    }

    public void setSearching(final boolean Searching) {
        post(new Runnable(){

				@Override
				public void run() {
					if (Searching) {
						mIndicatorView.setVisibility(VISIBLE);
						mActionIcon.setVisibility(GONE);
					} else {
						mIndicatorView.setVisibility(GONE);
						if (mEditText.getText() != null && mEditText.getText().length() > 0) {
							mActionIcon.setVisibility(VISIBLE);
						} else {
							mActionIcon.setVisibility(GONE);
						}
					}
				}

			});
    }

    public void requestEditTextFocus() {
        InputMethodManager imm =
			(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void addTintedUpNavigation() {
        setNavigationIcon(R.drawable.ic_search_arrow_back);
    }

    public interface SearchListener {
        void search(String text);
    }

    private SearchListener mSearchListener;

    public void setSearchListener(SearchListener searchListener) {
        mSearchListener = searchListener;
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setInputType(int typeTextFlag) {
        mEditText.setInputType(typeTextFlag);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public void clearText() {
        mEditText.setText(null);
    }

    public boolean hasText() {
        return mEditText.length() > 0;
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }
}

