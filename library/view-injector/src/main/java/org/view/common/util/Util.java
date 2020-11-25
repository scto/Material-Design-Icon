package org.view.common.util;
import android.annotation.SuppressLint;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;

public class Util {

    private static Application sApp;

    public static void setApp(Application app) {
        Util.sApp = app;
    }

    public static Application getApp() {
        if (sApp == null) {
            sApp = getApplicationByReflect();
        }
        return sApp;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
    
}
