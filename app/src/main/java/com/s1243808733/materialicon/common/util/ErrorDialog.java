package com.s1243808733.materialicon.common.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.s1243808733.materialicon.R;

public class ErrorDialog {

    public static AlertDialog show(Context context, Throwable t) {
        return show(context, context.getString(R.string.error_dialog_title), t);
    }

    public static AlertDialog show(final Context context, int title, final Throwable t) {
        return show(context, context.getResources().getString(title), t);
    }

    public static AlertDialog show(final Context context, CharSequence title, final Throwable t) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(t.getMessage())
            .setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    ClipboardUtils.copyText(t.getMessage());
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .setNeutralButton(context.getString(R.string.error_dialog_btn_detail), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dia, int which) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.error_dialog_title_detail))
                        .setMessage(ThrowableUtils.getFullStackTrace(t))
                        .setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                ClipboardUtils.copyText(ThrowableUtils.getFullStackTrace(t));
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create();
                    TextView message = dialog.findViewById(android.R.id.message);
                    if (message != null) {
                        message.setTextIsSelectable(true);
                    }
                }
            })
            .create();
        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null) {
            message.setTextIsSelectable(true);
        }
        return dialog;
    }

}

