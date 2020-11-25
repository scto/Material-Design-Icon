package com.s1243808733.materialicon.ui.fragment.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.data.model.OpenSourceLicense;
import java.util.ArrayList;
import java.util.List;
import org.view.ViewManager;
import org.view.annotation.ViewInject;

public class OpenSourceLicenseDialogFragment extends DialogFragment implements BaseQuickAdapter.OnItemClickListener {

    public static final String TAG = "OpenSourceLicenseDialogFragment";

    private OpenSourceAdapter mAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setId(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
            .setTitle(R.string.open_source_license)
            .setView(recyclerView)
            .setNegativeButton(R.string.close, null);
            
        AlertDialog dialog = builder.create();

        List<OpenSourceLicense> datas = new ArrayList<>();
        try {
            datas = new Gson().fromJson(ResourceUtils.readRaw2String((R.raw.open_souece_license)), new TypeToken<List<OpenSourceLicense>>(){}.getType());
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        mAdapter = new OpenSourceAdapter(datas);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);   
        return dialog;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OpenSourceLicense item = mAdapter.getItem(position);
        String url = item.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {}
    }
    
    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    private static class OpenSourceAdapter extends BaseQuickAdapter<OpenSourceLicense,OpenSourceAdapter.MyViewHolder> {

        public OpenSourceAdapter(List<OpenSourceLicense> datas) {
            super(R.layout.list_item_open_source_license,datas);   
        }

        @Override
        protected void convert(OpenSourceLicenseDialogFragment.OpenSourceAdapter.MyViewHolder holder, OpenSourceLicense item) {
            String url = item.getUrl();
            if (url.contains("github.com")) {
                holder.icon.setImageResource(R.drawable.ic_github_circle);
            } else {
                holder.icon.setImageDrawable(null);
            }

            holder.name.setText(item.getName());
            holder.author.setText(item.getAuthor());
            if (!TextUtils.isEmpty(item.getLicense())) {
                holder.author.append(" - ");
                holder.author.append(item.getLicense());
            }

            holder.describes.setText(item.getDescribes());

            TextView[] views={holder.name, holder.author,holder.describes};
            for (int i = 0; i < views.length; i++) {
                TextView tv = views[i];
                if (StringUtils.isTrimEmpty(tv.getText().toString())) {
                    tv.setVisibility(View.GONE);    
                } else {
                    tv.setVisibility(View.VISIBLE);
                }
            }
        }
        
        private static class MyViewHolder extends BaseViewHolder {

            @ViewInject(R.id.icon)
            public ImageView icon;

            @ViewInject(R.id.name)
            public TextView name;

            @ViewInject(R.id.author)
            public TextView author;

            @ViewInject(R.id.describes)
            public TextView describes;

            private MyViewHolder(View view) {
                super(view);
                ViewManager.view().inject(this,view);
            }
        }

    }

}

