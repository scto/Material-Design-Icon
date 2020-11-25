package com.s1243808733.materialicon.data.model;

import android.graphics.Bitmap;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.caverock.androidsvg.SVG;
import com.s1243808733.materialicon.data.SVGWrapper;
import com.s1243808733.materialicon.parse.IconPackageParser;
import java.io.InputStream;

public class IconInfo extends IconSection implements Comparable<IconInfo> {

	private static final long serialVersionUID = 1L;

    private final IconMetaData iconMetaData;

    private CharSequence name;
    
    private Bitmap icon;

    private SVGWrapper svgWrap;

    private String svgData;

    public IconInfo(IconMetaData iconMetaData) {
        IconInfo.this.t = this;
        this.iconMetaData = iconMetaData;
        this.name = iconMetaData.getName();
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setSvgData(String svgData) {
        this.svgData = svgData;
    }

    public String getSvgData() {
        if (svgData != null) return svgData;
        try {
            InputStream is = IconPackageParser.getInstance().getSvgIS(iconMetaData.getName() + ".svg");
            svgData = ConvertUtils.inputStream2String(is, "utf-8");
        } catch (Throwable e) {
            svgData = "";
            LogUtils.e(e);
        }
        return svgData;
    }

    public void setSvgWarp(SVGWrapper svg) {
        this.svgWrap = svg;
    }

	public boolean isSvgLoaded() {
		return svgWrap != null;
	}

    public SVGWrapper getSvgWrap() {
        if (svgWrap != null) return svgWrap;
        try {
			SVG svg = SVG.getFromString(getSvgData());
            svgWrap = new SVGWrapper(svg);
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return svgWrap;
    }

    public SVG getSvg() {
        return getSvgWrap().getSvg();
    }

    public IconMetaData getIconMetaData() {
        return iconMetaData;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public CharSequence getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (getClass() == IconInfo.class) {
            IconInfo info = (IconInfo) obj;
            return iconMetaData.equals(info.iconMetaData);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return iconMetaData.hashCode();
    }

    @Override
    public int compareTo(IconInfo p1) {
        return getName().toString().compareTo(p1.getName().toString());
    }

}
