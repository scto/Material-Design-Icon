package com.s1243808733.materialicon.common.util;

import android.view.View;
import java.math.BigDecimal;

public class Helper {

    public static String double2String(double val) {
        BigDecimal bd = new BigDecimal(String.valueOf(val));
        return bd.stripTrailingZeros().toPlainString();
    }

    public static View getParentView(View view, int viewId) {
        if (view.getId() == viewId) 
            return view;
        while ((view = (View) view.getParent()) != null) {
            if (view.getId() == viewId) {
                return view;
            }
        }
        return null;
    }
    
}
