package com.s1243808733.materialicon.ui.fragment.dialog;

import android.os.Bundle;
import com.blankj.utilcode.util.LogUtils;
import com.s1243808733.materialicon.data.IconDetailData;
import com.s1243808733.materialicon.data.SVGWrapper;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconMetaData;
import com.s1243808733.materialicon.ui.fragment.dialog.base.BaseDialogFragment;

public abstract class IconGenerateDialogFragment extends BaseDialogFragment {

    private IconArguments args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = new IconArguments(getArgsOrCreate());
    }

    public final IconArguments getArgs() {
        return args;
    }

    public static <F extends IconGenerateDialogFragment> F newInstance(
        Class<F> kind,
        IconDetailData data) {
        try {
            F fragment = kind.newInstance();
            Bundle fragmentArgs = fragment.getArgsOrCreate();
            fragmentArgs.putParcelable(AppConstants.EXTRA_ICON_DETAIL_DATA, data);
            return fragment;
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return null;
    }

    public class IconArguments {

        public final IconDetailData iconDetailData;

        public final IconMetaData iconMetaData;

        public final String iconName;

        public final IconInfo iconInfo;;

        public final SVGWrapper svgWarp;

        public IconArguments(Bundle args) {
            iconDetailData = args.getParcelable(AppConstants.EXTRA_ICON_DETAIL_DATA);
            iconMetaData = iconDetailData.getIconMetaData();
            iconName = iconMetaData.getName();
            iconInfo = iconDetailData.getIconInfo();
            svgWarp = iconDetailData.getSvgWrap();
        }

        public String getFileName() {
            return iconName + ".svg";
        }

    }

}
