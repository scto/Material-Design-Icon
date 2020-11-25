package com.s1243808733.materialicon.common.util;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

public class ThemeUtils {
	
	public static int getColor(Context context,int attrId) {
		return getColor(context,attrId,0);
	}
	
    public static int getColor(Context context,int attrId,int def) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrId});
        int color = a.getColor(0, def);
        a.recycle();
        return color;
    }
	
	public static Drawable getDrawable(Context context,int attrId) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrId});
        Drawable drawable = a.getDrawable(0);
        a.recycle();
        return drawable;
    }
	
    public static int getColorAccent(Context context) {
        return getColor(context,android.R.attr.colorAccent);
    }
    
    public static int getTextColorPrimary(Context context) {
     return getColor(context,android.R.attr.textColorPrimary);
    }
	
    public static int getTextColorTertiary(Context context) {
       return getColor(context,android.R.attr.textColorTertiary);
    }
	
    public static int getColorControlNormal(Context context) {
       return getColor(context,android.R.attr.colorControlNormal);
    }
    
}
