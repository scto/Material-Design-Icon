package com.s1243808733.materialicon.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.core.content.ContextCompat;
import androidx.collection.LruCache;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.common.util.BitmapUtils;
import com.s1243808733.materialicon.data.IconSectionMetaData;
import com.s1243808733.materialicon.data.SVGWrapper;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.data.model.IconSection;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import java.util.List;
import com.blankj.utilcode.util.StringUtils;

public class IconAdapter extends BaseSectionQuickAdapter<IconSection, BaseViewHolder> 
implements FastScrollRecyclerView.SectionedAdapter {

    private final Context mContext;

    private IconSectionMetaData sectionData;

    private String mCurrentSection;

    private final LruCache<IconInfo, Bitmap> mIconLruCache;

    public IconAdapter(Context context) {
        this(context, R.layout.list_item_icon_materialicon, R.layout.list_item_icon_section);
    }

    public IconAdapter(Context context, int layoutResId, int sectionHeadResId) {
        super(layoutResId, sectionHeadResId, null);
        this.mContext = context;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 5;
        mIconLruCache = new LruCache<IconInfo, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(IconInfo key, Bitmap bitmap) {
                return bitmap.getRowBytes();
            }
        };

    }

    public String getSection() {
        return mCurrentSection;
    }

    public void setSectionData(IconSectionMetaData sectionData) {
        this.sectionData = sectionData;
    }

    public IconSectionMetaData getSectionData() {
        return sectionData;
    }

    public void setShowData(List<IconSection> datas) {
        setNewData(datas);
    }

    public List<IconSection> getShowData() {
        return getData();
    }

    public void setSection(String section) {
        if (mCurrentSection != null && mCurrentSection.equals(section)) {
            return;
        }
        mCurrentSection = section;

        if (section != null && sectionData.hasSection(section)) {
            List<IconSection> icon = sectionData.getIconData(section).getIconSections();
            setShowData(icon);
        } else {
            setShowData(sectionData.getAllIconData().getIconSections());
        }
    }

    @Override
    protected void convertHead(BaseViewHolder helper, IconSection item) {
        helper.setText(R.id.title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, IconSection convertItem) {
        IconInfo item = convertItem.t;
		ImageView iconView = helper.getView(R.id.svg_view);

        Bitmap cache = mIconLruCache.get(item);
        if (cache == null) {
            new LoadIconTask(helper).execute(item);
        } else {
            iconView.setImageBitmap(cache);
        }

        helper.setText(R.id.name, item.getName());

        helper.convertView.setContentDescription(item.getName());
    }

    @Override
    public String getSectionName(int position) {
        IconSection convertItem = getItem(position);
        if (convertItem instanceof IconInfo) {
            IconInfo item = (IconInfo) convertItem;
            CharSequence name = item.getName();
            if (StringUtils.isEmpty(name)) {
                return "*";
            } else if (!StringUtils.isTrimEmpty(name.toString())) {
                return String.valueOf(Character.toUpperCase(name.charAt(0)));
            }
            return name.toString();
        }
        return convertItem.header;
    }

    private class LoadIconResult {
        public Bitmap icon;
        public IconInfo iconInfo;
    }

	private class LoadIconTask extends AsyncTask<IconInfo,Void,LoadIconResult> {

		private final BaseViewHolder helper;

		public LoadIconTask(BaseViewHolder helper) {
			this.helper = helper;
		}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ImageView iconView = helper.getView(R.id.svg_view);
            iconView.setImageDrawable(null);
        }

		@Override
		protected LoadIconResult doInBackground(IconInfo[] args) {
            IconInfo iconInfo = args[0];
            SVGWrapper wrap = iconInfo.getSvgWrap();
            LoadIconResult result = new LoadIconResult();
            Bitmap bit = wrap.createBitmap(mContext.getResources().getDimensionPixelSize(R.dimen.icon_grid_item_image_size));
            result.icon = BitmapUtils.drawColorExtractAlpha(bit, ContextCompat.getColor(mContext, R.color.icon_grid_item), true);
            result.iconInfo = iconInfo;
            return result;
		}

		@Override
		protected void onPostExecute(LoadIconResult result) {
            Bitmap icon = result.icon;
            super.onPostExecute(result);
            mIconLruCache.put(result.iconInfo, icon);
            ImageView iv = helper.getView(R.id.svg_view);
            iv.setImageBitmap(icon);
		}

	}

}
