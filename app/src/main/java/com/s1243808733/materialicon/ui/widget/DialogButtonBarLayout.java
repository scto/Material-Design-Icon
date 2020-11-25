package com.s1243808733.materialicon.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.widget.ButtonBarLayout;
import com.s1243808733.materialicon.R;

@SuppressWarnings("RestrictedApi")
public class DialogButtonBarLayout extends ButtonBarLayout {

    private final int HORIZONTAL_MARGIN;
    private final int VERTICAL_MARGIN;

    public DialogButtonBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.HORIZONTAL_MARGIN = getResources().getDimensionPixelOffset(R.dimen.dialog_btn_margin_horizontal);
        this.VERTICAL_MARGIN = getResources().getDimensionPixelOffset(R.dimen.dialog_btn_margin_vertical);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        adjutButtonMargin(getOrientation() == LinearLayout.VERTICAL);
    }

    private void adjutButtonMargin(boolean stacked) {
        int childCount = getChildCount();

        int visibilityCount = 0; 
        getVisibilityCount: {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() == VISIBLE) {
                    ++visibilityCount;
                }
            }
        }
        
        adjutButtonMargin: {
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                if (!(view instanceof Button)) {
                    continue;
                }
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = 0;
                lp.topMargin = 0;
                lp.rightMargin = 0;
                lp.bottomMargin = 0;
                
                if (stacked) {
                    if (i == 0) {
                        lp.topMargin = VERTICAL_MARGIN;
                    }
                    lp.bottomMargin = VERTICAL_MARGIN;
                } else {
                    if (visibilityCount > 1 && i + 1 < childCount) {
                        lp.rightMargin = HORIZONTAL_MARGIN;
                    }
                }
                view.setLayoutParams(lp);
            }

        }
    }


}

