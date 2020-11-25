package org.view;
import android.app.Activity;
import android.app.Application;
import java.util.HashSet;
import org.view.common.util.LogUtil;
import org.view.common.util.Util;

/**
 * author: s1243808733
 * date: 2020/08/08 22:52
 */
public class ViewManager {

    private static ViewManager sManager;

    private ViewInjector mInjector;
	
    private ViewManager() {
    }

    public void setInjector(ViewInjector viewInjector) {
        this.mInjector = viewInjector;
    }

    public ViewInjector getInjector() {
        return mInjector;
    }

    public static ViewInjector view() {
        ViewManager manager = getInstance();
        if (manager.mInjector == null) {
            manager.mInjector = ViewInjectorImpl.getInstance();
        }
        return manager.getInjector();
    }

    public static ViewManager getInstance() {
        if (sManager == null) {
            sManager = new ViewManager();
        }
        return sManager;
    }

    public static class Ext {
        public static void init(Application app) {
            Util.setApp(app);
        }
    }

}
