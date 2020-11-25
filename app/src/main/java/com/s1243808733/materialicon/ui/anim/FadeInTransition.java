package com.s1243808733.materialicon.ui.anim;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/4
 */

import androidx.transition.AutoTransition;
import androidx.transition.Transition;

public class FadeInTransition extends AutoTransition {
    private static final int FADE_IN_DURATION = 250;

    private FadeInTransition() {

    }

    public static Transition createTransition() {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(FADE_IN_DURATION);
        return transition;
    }
}
