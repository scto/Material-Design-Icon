package com.s1243808733.materialicon.data.constant;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.blankj.utilcode.util.Utils;
import java.io.File;

public class AppConstants {
    
    public static final String ASSETS_ICON;

    public static final String ASSETS_ICON_CONFIGS_FILE;

    public static final String EXTRA_ARGS = "EXTRA_ARGS";

    public static final String EXTRA_ICON_DETAIL_DATA = "EXTRA_ICON_DETAIL_DATA";

    public static final String ICON_EXPORT_FILENAME = "MaterialIcon";

    public static final File ICON_IMAGE_EXPORT_PATH;

    public static final File ICON_VECTOR_EXPORT_PATH;

    public static final File ICON_SVG_EXPORT_PATH;

    public static final File ICON_EXPORT_PATH;

	static {
        ASSETS_ICON = "icon_pkg";
        ASSETS_ICON_CONFIGS_FILE = ASSETS_ICON + File.separator + "configs.prop";

        ICON_EXPORT_PATH = new File(new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES), ICON_EXPORT_FILENAME);

        ICON_IMAGE_EXPORT_PATH = new File(ICON_EXPORT_PATH, "image");
        ICON_VECTOR_EXPORT_PATH = new File(ICON_EXPORT_PATH, "vector");
        ICON_SVG_EXPORT_PATH = new File(ICON_EXPORT_PATH, "svg");

	}

}
