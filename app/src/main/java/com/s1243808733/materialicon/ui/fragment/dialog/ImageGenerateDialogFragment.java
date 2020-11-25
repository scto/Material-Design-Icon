package com.s1243808733.materialicon.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caverock.androidsvg.SVG;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.common.util.BitmapUtils;
import com.s1243808733.materialicon.common.util.Helper;
import com.s1243808733.materialicon.common.util.SimpleTextWatcher;
import com.s1243808733.materialicon.common.util.ThemeUtils;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.data.model.IconInfo;
import com.s1243808733.materialicon.ui.view.viewholder.ViewHolder;
import com.s1243808733.materialicon.ui.widget.ColorBackgroundTextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.view.ViewManager;
import org.view.annotation.Event;
import org.view.annotation.ViewInject;

public class ImageGenerateDialogFragment extends IconGenerateDialogFragment {

    public static final String TAG = "ImageGenerateDialogFragment";

    private FragmentController mController;

	private ContentController mContentController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new FragmentController(savedInstanceState);
        mContentController = new ContentController(new ContentViewHolder(mController.contentView));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mController.dialog;
    }

    private class FragmentController implements DialogInterface.OnShowListener {

        public final AlertDialog dialog;

        public final View contentView;

        public FragmentController(Bundle savedInstanceState) {
            contentView = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_fragment_image_generate, null, false);
            dialog = createDialog();
        }

        public AlertDialog getDialog() {
            return dialog;
        }

        public View getContentView() {
            return contentView;
        }

        private AlertDialog createDialog() {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(createDialogIcon())
                .setTitle(R.string.export_to_image)
                .setView(contentView)
                .setPositiveButton(R.string.export, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
            dialog.setOnShowListener(this);
            return dialog;
        }

        @Event(value = android.R.id.button1)
        void onPositiveButtonClick(View view) {

            ContentViewHolder contentViewHolder = mContentController.viewHolder;
            String name = contentViewHolder.name.getText().toString();
            String iconWeightStr = contentViewHolder.icon_width.getText().toString();
            String iconHeightStr = contentViewHolder.icon_height.getText().toString();
            int iconColor = contentViewHolder.select_color.getColor();
            boolean use_tint = contentViewHolder.use_tint.isChecked();
            String exportPath = contentViewHolder.export_path.getText().toString();

            if (StringUtils.isTrimEmpty(name)) {
                contentViewHolder.name.setError(getString(R.string.message_please_input_icon_name));
                return;
            } else if (StringUtils.isTrimEmpty(iconWeightStr)) {
                contentViewHolder.icon_width.setError(getString(R.string.message_please_input_icon_width));
                return;
            } else if (StringUtils.isTrimEmpty(iconHeightStr)) {
                contentViewHolder.icon_height.setError(getString(R.string.message_please_input_icon_height));
                return;
            } else if (StringUtils.isTrimEmpty(exportPath)) {
                contentViewHolder.export_path.setError(getString(R.string.message_please_input_export_path));
                return;
            }

            int iconWeight = 0;
            int iconHeight = 0;
            try {
                iconWeight = Integer.parseInt(iconWeightStr);
            } catch (NumberFormatException ignored) {}
            try {
                iconHeight = Integer.parseInt(iconHeightStr);
            } catch (NumberFormatException ignored) {}

            if (iconWeight <= 0) {
                contentViewHolder.icon_width.setError(getString(R.string.message_icon_width_cannot_be_0));
                return;
            } else if (iconHeight <= 0) {
                contentViewHolder.icon_height.setError(getString(R.string.message_icon_height_cannot_be_0));
                return;
            } 

            File exportPathFile = new File(exportPath);
            if (exportPathFile.isFile()) {
                contentViewHolder.export_path.setError(getString(R.string.message_path_not_is_directory));
                return;
            }

            List<IconGenInfo> icons = new ArrayList<>(); 

            add: {
                IconGenInfo info = new IconGenInfo();
                info.name = name;
                info.width = iconWeight;
                info.height = iconHeight;
                info.color = iconColor;
                info.tint = use_tint;
                info.exportPath = exportPathFile;
                info.format = ".png";
                icons.add(info);
            }

            checkPermissionAndSave(icons);
        }

        private void checkPermissionAndSave(final List<IconGenInfo> exportInfo) {
            if (PermissionUtils.isGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                saveAndDismiss(exportInfo);
                return;
            }
            PermissionUtils.permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.FullCallback(){

                    private boolean recheckPermission;

                    @Override
                    public void onGranted(List<String> granted) {
                        LogUtils.i("onGranted");
                        saveAndDismiss(exportInfo);
                    }

                    @Override
                    public void onDenied(final List<String> deniedForever, List<String> denied) {
                        LogUtils.i("onDenied");
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.title_no_write_permission)
                            .setMessage(R.string.message_no_write_permission)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    if (deniedForever.size() != 0) {
                                        PermissionUtils.launchAppDetailsSettings();
                                    } else {
                                        recheckPermission = true;
                                        checkPermissionAndSave(exportInfo);
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .create();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                                @Override
                                public void onDismiss(DialogInterface dia) {
                                    LogUtils.iTag("recheckPermission", recheckPermission);
                                    if (!recheckPermission) FragmentController.this.dialog.show();
                                }
                            });
                        FragmentController.this.dialog.hide();
                        dialog.show();
                    }

                }).request();
        }
        
        private void saveAndDismiss(List<IconGenInfo> icons) {

            gen: {
                for (int i = 0; i < icons.size(); i++) {
                    IconGenInfo info = icons.get(i);
                    if (!info.exportPath.exists()) info.exportPath.mkdirs();
                    if (!info.exportPath.canWrite()) {
                        ToastUtils.showShort(R.string.message_path_not_writable);
                        return;
                    }
                    IconArguments args = getArgs();
                    Bitmap bitmap = args.iconInfo.getSvgWrap().createBitmap(info.width, info.height);
                    if (info.tint) {
                        bitmap = BitmapUtils.drawColorExtractAlpha(bitmap, info.color, true);
                    }
                    File exportFile = info.toFile();
                    ImageUtils.save(bitmap, exportFile, Bitmap.CompressFormat.PNG, true);
                }
            }

            finishDialog: {
                StringBuffer message = new StringBuffer();
                for (int i = 0; i < icons.size(); i++) {
                    IconGenInfo info = icons.get(i);
                    if (i > 0) message.append("\n");
                    message.append(info.toFile().getAbsolutePath());
                }
                this.dialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_saved_to)
                    .setMessage(message)
                    .setNegativeButton(R.string.close, null)
                    .create();
                dialog.show();
            }

        }

        @Event(value = android.R.id.button2)
        void onNegativeButtonClick(View view) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        }

        @Override
        public void onShow(DialogInterface dia) {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            ViewManager.view().inject(this, Helper.getParentView(positiveButton, R.id.parentPanel));
        }

        private Drawable createDialogIcon() {
            Picture pic = getArgs().iconDetailData.getSvgWrap().createPicture(96);
            Bitmap bitmap = BitmapUtils.drawColorExtractAlpha(
                ImageUtils.drawable2Bitmap(new PictureDrawable(pic)),
                ThemeUtils.getTextColorPrimary(getActivity()), true);
            return new BitmapDrawable(bitmap);
        }

    }

    private class ContentController {

        public final ContentViewHolder viewHolder;

        public ContentController(ContentViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            initView();
        }

        public void initView() {
            ViewManager.view().inject(this, viewHolder.view);

            IconInfo iconInfo = getArgs().iconInfo;
            SVG svg = iconInfo.getSvg();

            viewHolder.name.requestFocus();
            viewHolder.name.setText(iconInfo.getName());
            viewHolder.name.setSelection(viewHolder.name.length());

            viewHolder.select_color.setColor(Color.parseColor("#FFFFFFFF"));

            viewHolder.icon_width.setText(Helper.double2String(svg.getDocumentWidth()));
            viewHolder.icon_height.setText(Helper.double2String(svg.getDocumentHeight()));

            if (svg.getDocumentWidth() == svg.getDocumentHeight()) {
                viewHolder.icon_width.addTextChangedListener(new SimpleTextWatcher(){
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!viewHolder.icon_height.getText().toString().equals(s.toString())) {
                                viewHolder.icon_height.setText(s);   
                            }
                        }
                    });
                viewHolder.icon_height.addTextChangedListener(new SimpleTextWatcher(){
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!viewHolder.icon_width.getText().toString().equals(s.toString())) {
                                viewHolder.icon_width.setText(s);   
                            }
                        }
                    });
            }

            viewHolder.export_path.setText(AppConstants.ICON_IMAGE_EXPORT_PATH.getAbsolutePath());
        }

        @Event(values=R.id.select_color)
        void showSelectColorDialog() {
            ColorPickerDialogBuilder builder = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle(R.string.title_choose_color)
                .initialColor(viewHolder.select_color.getColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .showColorEdit(true)
                .showColorPreview(true)
                .setColorEditTextColor(ThemeUtils.getTextColorPrimary(getActivity()))
                .setPositiveButton(R.string.choose, new ColorPickerClickListener(){

                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        viewHolder.select_color.setColor(lastSelectedColor);   
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                    }
                });

            AlertDialog colorPickerDialog = builder.build();
            colorPickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface dia) {
                        mController.dialog.show();
                    }
                });
            mController.dialog.hide();
            colorPickerDialog.show();
        }

        @Event(values=R.id.use_tint,
            type = CompoundButton.OnCheckedChangeListener.class,
            setter = "setOnCheckedChangeListener")
        void tintCheckedChanged(CompoundButton btn, boolean checked) {
            viewHolder.select_color.setEnabled(checked);
        }

    }

    private static class IconGenInfo {
        public String name;
        public int width,height;
        public int color;
        public boolean tint;
        public File exportPath;
        public String format;

        public File toFile() {
            return new File(exportPath, name + format);
        }
    }

    private static class ContentViewHolder extends ViewHolder {

        @ViewInject(R.id.name)
        public AppCompatEditText name;

        @ViewInject(R.id.icon_width)
        public AppCompatEditText icon_width;

        @ViewInject(R.id.icon_height)
        public AppCompatEditText icon_height;

        @ViewInject(R.id.select_color)
        public ColorBackgroundTextView select_color;

        @ViewInject(R.id.use_tint)
        public AppCompatCheckBox use_tint;

        @ViewInject(R.id.export_path)
        public AppCompatEditText export_path;

		public ContentViewHolder(View view) {
			super(view);
		}

	}

}
