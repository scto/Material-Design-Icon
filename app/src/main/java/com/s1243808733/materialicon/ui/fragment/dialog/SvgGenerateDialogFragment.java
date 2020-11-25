package com.s1243808733.materialicon.ui.fragment.dialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.s1243808733.materialicon.R;
import com.s1243808733.materialicon.common.util.ErrorDialog;
import com.s1243808733.materialicon.common.util.Helper;
import com.s1243808733.materialicon.data.constant.AppConstants;
import com.s1243808733.materialicon.parse.IconPackageParser;
import com.s1243808733.materialicon.ui.view.viewholder.ViewHolder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.view.ViewManager;
import org.view.annotation.Event;
import org.view.annotation.ViewInject;

public class SvgGenerateDialogFragment extends IconGenerateDialogFragment implements DialogInterface.OnShowListener {

    public static final String TAG = "SvgGenerateDialogFragment";

    private MyViewHolder mViewHolder;

    private AlertDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = getActivity().getLayoutInflater()
            .inflate(R.layout.dialog_fragment_svg_generate, null, false);
        mViewHolder = new MyViewHolder(contentView);
        mViewHolder.fileName.setText(getArgs().getFileName());
        mViewHolder.fileName.setSelection(mViewHolder.fileName.length());

        mViewHolder.export_path.setText(AppConstants.ICON_SVG_EXPORT_PATH.getAbsolutePath());

        mDialog = new AlertDialog.Builder(getActivity())
            .setTitle(R.string.export_to_svg)
            .setView(contentView)
            .setPositiveButton(R.string.export, null)
            .setNegativeButton(R.string.cancel, null)
            .create();
        mDialog.setOnShowListener(this);
        return mDialog;
    }

    @Override
    public void onShow(DialogInterface dia) {
        Button positiveButton = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ViewManager.view().inject(this, Helper.getParentView(positiveButton, R.id.parentPanel));
        mViewHolder.fileName.requestFocus();
    }

    @Event(value = android.R.id.button1)
    void onPositiveButtonClick(View view) {
        String fileName = mViewHolder.fileName.getText().toString();
        String exportPath = mViewHolder.export_path.getText().toString();
        if (StringUtils.isTrimEmpty(exportPath)) {
            ToastUtils.showShort(R.string.message_please_input_export_path);
            return;
        }
        ExportInfo exportInfo = new ExportInfo();
        exportInfo.fileName = fileName;
        exportInfo.exportPath = FileUtils.getFileByPath(exportPath);
        try {
            exportInfo.data = IconPackageParser.getInstance().readSvg(getArgs().getFileName());
        } catch (IOException e) {
            ErrorDialog.show(getActivity(), e);
            mDialog.dismiss();
            return;
        }
        checkPermissionAndSave(exportInfo);
    }

    private void checkPermissionAndSave(final ExportInfo exportInfo) {
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
                                if (!recheckPermission) mDialog.show();
                            }
                        });
                    mDialog.hide();
                    dialog.show();
                }

            }).request();
    }

    private void saveAndDismiss(ExportInfo exportInfo) {
        File exportPath = exportInfo.exportPath;
        if (!exportPath.exists()) {
            exportPath.mkdirs();
        } else if (!exportPath.isDirectory()) {
            ToastUtils.showShort(R.string.message_path_not_is_directory);
            return;
        }
        if (!exportPath.canWrite()) {
            ToastUtils.showShort(R.string.message_path_not_writable);
            return;
        }

        File exportFile = new File(exportPath, exportInfo.fileName);
        LogUtils.iTag("exportFile", exportFile.getAbsolutePath());
        if (FileIOUtils.writeFileFromString(exportFile, exportInfo.data)) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_saved_to)
                .setMessage(exportFile.getAbsolutePath())
                .setNegativeButton(R.string.close, null)
                .create();
            dialog.show();
        } else {
            ToastUtils.showShort(R.string.export_failed);
        }
        mDialog.dismiss();
    }

    @Event(value = android.R.id.button2)
    void onNegativeButtonClick(View view) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    private static class ExportInfo {
        public String fileName;
        public File exportPath;
        public String data;
    }

    private static class MyViewHolder extends ViewHolder {

        @ViewInject(R.id.fileName)
        public EditText fileName;

        @ViewInject(R.id.export_path)
        public EditText export_path;

        public MyViewHolder(View view) {
            super(view);
        }
    }

}
