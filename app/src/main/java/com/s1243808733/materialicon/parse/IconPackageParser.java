package com.s1243808733.materialicon.parse;
import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.data.model.IconMetaData;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipFile;

public class IconPackageParser implements Closeable {

    public static IconPackageParser sParser;

    public final Properties mProperties;

    public final File file;
    public final ZipFile zipFile;

    public final String ZIP_MAIN_DIR;
    public final String ZIP_SVG_DIR;
    public final String ZIP_META_FILE;

    private List<IconMetaData> mMetaDatas;

    public IconPackageParser(File configFile) throws IOException  {
        mProperties = new Properties();
        mProperties.load(new FileInputStream(configFile));
        file = new File(configFile.getParentFile(), mProperties.getProperty("file", ""));
        zipFile = new ZipFile(file);

        ZIP_MAIN_DIR = mProperties.getProperty("zip.main", "");
        ZIP_SVG_DIR = ZIP_MAIN_DIR + mProperties.getProperty("zip.svg");
        ZIP_META_FILE = ZIP_MAIN_DIR + mProperties.getProperty("zip.meta");
    }

	public Properties getProperties() {
		return mProperties;
	}

    public static IconPackageParser getInstance() {
        if (sParser == null) {
            File outDir = Utils.getApp().getExternalFilesDir("icon");
            File config = new File(outDir, AppConstants.ASSETS_ICON_CONFIGS_FILE);
            FileUtils.deleteAllInDir(outDir);
            if (!ResourceUtils.copyFileFromAssets(AppConstants.ASSETS_ICON, new File(outDir, AppConstants.ASSETS_ICON).getAbsolutePath())) {
                throw new RuntimeException("Failed to copy files from asset");
            }
            try {
                sParser = new IconPackageParser(config);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sParser;
    }

    public String getMetaData() {
        try {
            return readFile(ZIP_META_FILE);
        } catch (IOException e) {
            LogUtils.e(e);
        }
        return "[]";
    }

    public List<IconMetaData> getMetaDatas() {
        return mMetaDatas == null ?(mMetaDatas = getNewMetaDatas()): mMetaDatas;
    }

	public List<IconMetaData> getNewMetaDatas() {
		Gson gson = new Gson();
		List<IconMetaData> datas = gson.fromJson(getMetaData(), new TypeToken<List<IconMetaData>>(){}.getType());
        return datas;
    }

    public InputStream getSvgIS(String name) throws IOException {
        return getFileIS(ZIP_SVG_DIR, name);
    }

    public String readSvg(String name) throws IOException {
        return ConvertUtils.inputStream2String(getSvgIS(name), "utf-8");
    }

    public String readFile(String name) throws IOException {
        return ConvertUtils.inputStream2String(getFileIS(name), "utf-8");
    }

    public InputStream getFileIS(String name) throws IOException {
        return getFileIS("/", name);
    }

    public InputStream getFileIS(String parent, String child) throws IOException {
        String file = new File(parent, child).getAbsolutePath().substring(1);
        return zipFile.getInputStream(zipFile.getEntry(file));
    }

    @Override
    public void close() {
        CloseUtils.closeIO(zipFile);
    }

}
