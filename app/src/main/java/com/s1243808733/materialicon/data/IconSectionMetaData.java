package com.s1243808733.materialicon.data;

import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconMetaData;
import com.s1243808733.materialicon.data.model.IconSection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.Serializable;

public class IconSectionMetaData implements Serializable{
    
	private static final long serialVersionUID = 1L;
	
    public static final String SECTION_ALL ;

    public static final String SECTION_OTHER ;

	public LinkedHashMap<String, IconDatas> getIconSectionMap() {
		return mSectionMap;
	}

    static{
        SECTION_ALL = "All";
        SECTION_OTHER = "Other";
    }

    private final LinkedHashMap<String,IconDatas> mSectionMap;

    public IconSectionMetaData(final List<IconMetaData> iconMetaDatas) {

		LinkedHashMap<String,IconDatas> sectionMap = new LinkedHashMap<>();

		if (iconMetaDatas != null && !iconMetaDatas.isEmpty()) {
			sectionMap.put(SECTION_ALL, new IconDatas());
			List<IconInfo> otherIcons = new ArrayList<>();

			for (IconMetaData metaData : iconMetaDatas) {
				if (metaData.getTags().isEmpty()) {
					IconInfo icon = new IconInfo(metaData);
					icon.setName(metaData.getName());
					otherIcons.add(icon);
					continue;
				}

				for (String section : metaData.getTags()) {
					IconInfo icon = new IconInfo(metaData);
					icon.setName(metaData.getName());

					if (sectionMap.containsKey(section)) {
						IconDatas datas = sectionMap.get(section);
						datas.getIconSections().add(icon);
						datas.getIconInfos().add(icon);
					} else {
						IconDatas.Builder datasBuilder = new IconDatas.Builder();
						datasBuilder.add(new IconSection(section));
						datasBuilder.add(icon);
						sectionMap.put(section, datasBuilder.create());
					}
				}
			}

			if (!otherIcons.isEmpty()) {
				IconDatas.Builder datasBuilder = new IconDatas.Builder();
				datasBuilder.add(new IconSection(SECTION_OTHER));
				for (IconInfo icon:otherIcons) {
					datasBuilder.add(icon);
				}
				sectionMap.put(SECTION_OTHER, datasBuilder.create());
			}

			final IconDatas allDatas = new IconDatas();
			for (String section : sectionMap.keySet()) {
				if (section.equals(SECTION_ALL)) continue;
				IconDatas data = sectionMap.get(section);
				allDatas.getIconSections().addAll(data.getIconSections());
				allDatas.getIconInfos().addAll(data.getIconInfos());
			}
			if (allDatas.getIconInfos().size() <= 1 && sectionMap.keySet().toArray(new String[]{}).length > 1) {
				sectionMap.remove(SECTION_ALL);
			} else {
				sectionMap.put(SECTION_ALL, allDatas);
			}
		}

		mSectionMap = sectionMap;
    }

	public IconDatas getIconData(String section) {
		return mSectionMap.get(section);
	}

	public IconDatas getAllIconData() {
		return getIconData(SECTION_ALL);
	}

	public IconDatas getOtherIconData() {
		return getIconData(SECTION_OTHER);
	}

    public String[] getSections() {
        return mSectionMap.keySet().toArray(new String[]{});
    }

    public boolean hasSection(String section) {
        return mSectionMap.containsKey(section);
    }

	public void clear() {
		mSectionMap.clear();
	}

}
