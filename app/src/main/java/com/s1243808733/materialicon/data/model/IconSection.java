package com.s1243808733.materialicon.data.model;

import com.chad.library.adapter.base.entity.SectionEntity;

public class IconSection extends SectionEntity<IconInfo> {
	
	private static final long serialVersionUID = 1L;
	
    public IconSection(String title) {
        super(true, title);
    }

    protected IconSection() {
        super(null);
    }

}
