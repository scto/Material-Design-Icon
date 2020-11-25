package com.s1243808733.materialicon.ui.anim;
import android.view.View;
import android.view.ViewGroup;

public class ViewFader {

	public static void hideContentOf(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			viewGroup.getChildAt(i).setVisibility(View.GONE);
        }
    }

	public static void showContent(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

}
