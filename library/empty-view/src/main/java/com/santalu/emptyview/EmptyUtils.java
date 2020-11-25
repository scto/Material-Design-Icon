package com.santalu.emptyview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by santalu on 09/08/2017.
 */

@SuppressLint("ResourceType")
final class EmptyUtils {

  static Drawable getDrawable(Context context, int id) {
    return id <= 0 ? null : context.getResources().getDrawable(id);
  }

  static String getString(Context context, int id) {
    return id <= 0 ? null : context.getString(id);
  }

  static void setTextSize(TextView textView, float textSize) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
  }
}
