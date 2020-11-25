package com.s1243808733.materialicon.data;
import android.os.Parcel;
import android.os.Parcelable;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconMetaData;
import java.io.Serializable;

public class IconDetailData implements Parcelable, Serializable {

    private static final long serialVersionUID = 1L;

	private final IconMetaData iconMetaData;

    private final String svgData;

    private IconInfo IconInfo;

    protected IconDetailData(final Parcel in) {
		iconMetaData = in.readParcelable(IconMetaData.class.getClassLoader());
		svgData = in.readString();
    }

	public IconDetailData(IconMetaData iconMetaData, String svgData) {
		this.iconMetaData = iconMetaData;
		this.svgData = svgData;
	}

    public IconInfo getIconInfo() {
        if (IconInfo == null) {
            IconInfo = new IconInfo(getIconMetaData());
        }
        return IconInfo;
    }

	public SVGWrapper getSvgWrap() {
		return getIconInfo().getSvgWrap();
	}

	public IconMetaData getIconMetaData() {
		return iconMetaData;
	}

	public String getSvgData() {
		return svgData;
	}

	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(iconMetaData, 0);
		dest.writeString(svgData);
    }

    public static final Creator<IconDetailData> CREATOR = new IconDetailDataCreator();

    public static final class IconDetailDataCreator implements Creator<IconDetailData> {

        @Override
        public IconDetailData createFromParcel(Parcel source) {
            return new IconDetailData(source);
        }

        @Override
        public IconDetailData[] newArray(int size) {
            return new IconDetailData[size];
        }

    }

}
