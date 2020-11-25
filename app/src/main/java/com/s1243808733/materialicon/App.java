package com.s1243808733.materialicon;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatDelegate;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.Utils;
import com.s1243808733.materialicon.common.util.CrashUtils;
import com.s1243808733.materialicon.ui.activity.CrashActivity;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import me.weishu.reflection.Reflection;
import org.view.ViewManager;
import com.blankj.utilcode.util.ToastUtils;

public class App extends Application {

    private static App sApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Reflection.unseal(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.sApp = this;
        initUtils();
        ViewManager.Ext.init(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        installPatch();
    }

    private void installPatch() {
        File patch = new File(getFilesDir(), "patch/AppCompatDialog.dex");
        ResourceUtils.copyFileFromRaw(R.raw.app_compat_dialog, patch.getAbsolutePath());
        Hotfix.installPatch(this, patch);
    }

    private void initUtils() {
        Utils.init(this);

		File logDir = new File(getExternalCacheDir(), "log");
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(true);
        config.setLog2FileSwitch(true);
        config.setSaveDays(2);
        config.setDir(logDir);

		CrashUtils.init(new File(logDir, "crash"), new CrashUtils.OnCrashListener(){

				@Override
				public void onCrash(String crashInfo, Throwable e) {
					Intent intent = new Intent(getApp(), CrashActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("crashInfo", crashInfo);
					getApp().startActivity(intent);
					AppUtils.exitApp();
				}
			});

        ToastUtils make = ToastUtils.getDefaultMaker();
        make.setLeftIcon(R.mipmap.ic_launcher);
        
    }

    public static App getApp() {
        return sApp;
    }


    /**
     * 基于Android8.0   SDK26
     */
    static class Hotfix {

        public static void installPatch(Application application, File patch) {
            if (!patch.exists()) {
                return;
            }
            //获取当前应用的PathClassLoader
            ClassLoader classLoader = application.getClassLoader();

            try {
                //反射获取到DexPathList属性对象pathList;
                Field field = ReflectUtils.findField(classLoader, "pathList");
                Object pathList = field.get(classLoader);


                //3.1、把补丁包patch.dex转化为Element[]  (patch)
                Method method = ReflectUtils.findMethod(pathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class);
                //构建第一个参数
                List patchs = new ArrayList();
                patchs.add(patch);
                //构建第三个参数
                ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
                //执行
                Object[] patchElements = (Object[]) method.invoke(null, patchs, null, suppressedExceptions, classLoader);


                //3.2获得pathList的dexElements属性（old）
                Field dexElementsField = ReflectUtils.findField(pathList, "dexElements");
                Object[] dexElements = (Object[]) dexElementsField.get(pathList);

                //3.3、patch+old合并，并反射赋值给pathList的dexElements
                Object[] newElements = (Object[]) Array.newInstance(patchElements.getClass().getComponentType(), patchElements.length + dexElements.length);
                System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
                System.arraycopy(dexElements, 0, newElements, patchElements.length, dexElements.length);
                dexElementsField.set(pathList, newElements);

                LogUtils.i("installPatch Success");
            } catch (Throwable e) {
                LogUtils.eTag("installPatch failed", e);
            }

        }

        static class ReflectUtils {
            public static Field findField(Object obj, String name) throws NoSuchFieldException {
                Class cls = obj.getClass();
                while (cls != Object.class) {
                    try {
                        Field field = cls.getDeclaredField(name);
                        if (field != null) {
                            field.setAccessible(true);
                            return field;
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    cls = cls.getSuperclass();
                }
                throw new NoSuchFieldException(obj.getClass().getSimpleName() + " not find " + name);
            }

            public static Method findMethod(Object obj, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
                Class cls = obj.getClass();
                while (cls != Object.class) {
                    try {
                        Method method = cls.getDeclaredMethod(name, parameterTypes);
                        if (method != null) {
                            method.setAccessible(true);
                            return method;
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    cls = cls.getSuperclass();
                }
                throw new NoSuchMethodException(obj.getClass().getSimpleName() + " not find " + name);
            }
        }

    }

}
