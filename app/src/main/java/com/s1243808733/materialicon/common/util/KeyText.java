package com.s1243808733.materialicon.common.util;
import android.text.SpannableString;
import java.util.stream.IntStream;

public class KeyText extends SpannableString {

    private String key;
    
    public KeyText(String key, CharSequence source) {
        super(source);
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public IntStream chars() {
        return null;
    }

    @Override
    public IntStream codePoints() {
        return null;
    }
    
}
