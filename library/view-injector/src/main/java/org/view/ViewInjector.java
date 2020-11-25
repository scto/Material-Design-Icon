package org.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Set;
import org.view.manager.MenuManager;

/**
 * Created by wyouflf on 15/10/29.
 * view注入接口
 */

/**
 * Modify by s1243808733
 * Last modify date 2020/9/4

 * 提取修改自Xutils3
 * 持续升级中
 */

public interface ViewInjector {

    void inject(View view, InjectOptions type);

    void inject(View view);

    void inject(Activity activity, InjectOptions type);

    void inject(Activity activity);

    void inject(Object handler, View view, InjectOptions type);

    void inject(Object handler, View view);

    void inject(Object handler, Activity activity, InjectOptions type);

    void inject(Object handler, Activity activity);

    MenuManager inject(Object handler, Menu menu);

    MenuManager inject(Activity activity, Menu menu);

    MenuManager inject(Object handler, Menu menu, MenuInflater inflater);

    View inject(Object fragment, LayoutInflater inflater, ViewGroup container);
    
    View inject(Object fragment, LayoutInflater inflater, ViewGroup container, InjectOptions type);

	Set<Class<?>> getIgnoreClass();

	boolean isIgnoreClass(Class<?> kind);

	ViewInjector addIgnoreClass(Class<?> kind);

	ViewInjector removeIgnoreClass(Class<?> kind);

}

