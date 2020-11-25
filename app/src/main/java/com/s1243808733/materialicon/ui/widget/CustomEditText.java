package com.s1243808733.materialicon.ui.widget;

import android.content.Context;
import android.os.Handler;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditText extends AppCompatEditText {

    private CancelErrorHintTimer mTimer;

    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(null);
        super.setError(error);
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new CancelErrorHintTimer();
        mTimer.start();
    }

    private class CancelErrorHintTimer implements Runnable {

        private boolean cancel;

        public void cancel() {
            this.cancel = true;
        }

        public boolean isCancel() {
            return cancel;
        }

        public void start() {
            if (!isCancel())
                new Handler().postDelayed(this, 1500);
        }

        @Override
        public void run() {
            if (!isCancel())
                setError(null);
        }

    }

}

