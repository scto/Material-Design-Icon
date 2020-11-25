package com.s1243808733.materialicon.ui.fragment.dialog.base;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialogFragment extends DialogFragment {

    public Bundle getArgsOrCreate() {
        Bundle args = getArguments();
        if (args == null) {
            setArguments(args = new Bundle());
        }
        return args;
    }

}
