package com.s1243808733.materialicon.data;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconSection;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class IconDatas implements Serializable{
    
	private static final long serialVersionUID = 1L;
	
	private List<IconSection> iconSections;

    private List<IconInfo> iconInfos;

	public IconDatas() {
		this(new ArrayList<IconSection>(), new ArrayList<IconInfo>());
	}

	public IconDatas(List<IconSection> iconSections, List<IconInfo> iconInfos) {
		this.iconSections = iconSections;
		this.iconInfos = iconInfos;
	}

	public void setIconSections(List<IconSection> iconSections) {
		this.iconSections = iconSections;
	}

	public List<IconSection> getIconSections() {
		return iconSections;
	}

	public void setIconInfos(List<IconInfo> iconInfos) {
		this.iconInfos = iconInfos;
	}

	public List<IconInfo> getIconInfos() {
		return iconInfos;
	}

	public static class Builder {
		private List<IconSection> iconSections;

		private List<IconInfo> iconInfos;

		public Builder() {
			iconSections = new ArrayList<>();
			iconInfos = new ArrayList<>();
		}

		public void setIconSections(List<IconSection> iconSections) {
			this.iconSections = iconSections;
		}

		public List<IconSection> getIconSections() {
			return iconSections;
		}

		public void setIconInfos(List<IconInfo> iconInfos) {
			this.iconInfos = iconInfos;
		}

		public List<IconInfo> getIconInfos() {
			return iconInfos;
		}

		public Builder add(List<IconSection> sections) {
			for (IconSection section:sections) {
				add(section);
			}
			return this;
		}

		public Builder add(IconSection[] sections) {
			for (IconSection section:sections) {
				add(section);
			}
			return this;
		}

		public Builder add(IconSection section) {
			iconSections.add(section);
			if (section instanceof IconInfo) {
				iconInfos.add((IconInfo)section);
			}
			return this;
		}

		public IconDatas create() {
			return new IconDatas(iconSections, iconInfos);
		}

	}

}
