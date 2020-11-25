package org.view;

/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import org.view.annotation.ContentView;
import org.view.annotation.Event;
import org.view.annotation.MenuItemEvent;
import org.view.annotation.OptionsMenu;
import org.view.annotation.ViewInject;
import org.view.common.util.ObjectUtils;
import org.view.common.util.ThrowableUtils;
import org.view.common.util.Util;
import org.view.common.view.ViewFinder;
import org.view.common.view.ViewInfo;
import org.view.manager.EventManager;
import org.view.manager.MenuManager;

public class ViewInjectorImpl implements ViewInjector {

	private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();

	static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.ListActivity.class);

        IGNORED.add(android.preference.PreferenceActivity.class);
        IGNORED.add(android.preference.PreferenceFragment.class);
        IGNORED.add(android.app.Fragment.class);

        IGNORED.add(android.app.AlertDialog.class);
        IGNORED.add(android.app.Dialog.class);

		String[] ignores = {
			"android.support.v7.app.AppCompatActivity",
			"android.support.v4.app.Fragment",
			"android.support.v4.app.FragmentActivity",
			"android.support.v7.widget.RecyclerView$ViewHolder",

			"androidx.appcompat.app.AppCompatActivity",
			"androidx.fragment.app.Fragment",
			"androidx.fragment.app.FragmentActivity",
			"androidx.recyclerview.widget.RecyclerView$ViewHolder"
		};
		for (String name : ignores) {
			try {
				IGNORED.add(Class.forName(name));
			} catch (ClassNotFoundException ignored) {}
		}
    }

    private static final Object lock = new Object();
    private static volatile ViewInjectorImpl instance;

    private ViewInjectorImpl() {
    }

    public static ViewInjectorImpl getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ViewInjectorImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void inject(View view) {
        inject(view, InjectOptions.newInstance());
    }

    @Override
    public void inject(View view, InjectOptions options) {
        ObjectUtils.requireNonNull(view);
        injectObject(view, view.getClass(), new ViewFinder(view), options);
    }

    @Override
    public void inject(Activity activity) {
        inject(activity, InjectOptions.newInstance());
    }

    @Override
    public void inject(Activity activity,  InjectOptions options) {
        ObjectUtils.requireNonNull(activity);
        Class<?> handlerType = activity.getClass();
        if (options.isInjectContentView()) {
            injectContent(activity);
        }
        injectObject(activity, handlerType, new ViewFinder(activity), options);
    }

    @Override
    public void inject(Object handler, View view) {
        inject(handler, view, InjectOptions.newInstance());
    }

    @Override
    public void inject(Object handler, View view, InjectOptions options) {
        ObjectUtils.requireNonNull(handler, view);
        injectObject(handler, handler.getClass(), new ViewFinder(view), options);
    }

    @Override
    public void inject(Object handler, Activity activity) {
        inject(handler, activity, InjectOptions.newInstance());
    }

    @Override
    public void inject(Object handler, Activity activity, InjectOptions options) {
        ObjectUtils.requireNonNull(handler, activity);
        injectObject(handler, handler.getClass(), new ViewFinder(activity), options);
    }

    @Override
    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container) {
        return inject(fragment, inflater, container, InjectOptions.newInstance());
    }

    @Override
    public View inject(Object fragment, LayoutInflater inflater, ViewGroup container,  InjectOptions options) {
        ObjectUtils.requireNonNull(fragment, inflater);
        Class<?> handlerType = fragment.getClass();
        View view = injectContent(fragment, handlerType, inflater, container);
        injectObject(fragment, handlerType, new ViewFinder(view), options);
        return view;
    }

    @Override
    public MenuManager inject(Object handler, Menu menu) {
        ObjectUtils.requireNonNull(handler, menu);
        MenuManager manager = new MenuManager(menu);
        return inject(manager, handler, handler.getClass());
    }

    @Override
    public MenuManager inject(Activity activity, Menu menu) {
        return inject(activity, menu, activity.getMenuInflater());
    }

    @Override
    public MenuManager inject(Object handler, Menu menu, MenuInflater inflater) {
        ObjectUtils.requireNonNull(handler, menu, inflater);
        Class<?> handlerType = handler.getClass();
        OptionsMenu optionsMenu = findOptionsMenu(handlerType);
        if (optionsMenu != null) {
            int menuRes = optionsMenu.value();
            if (menuRes > 0) {
                inflater.inflate(menuRes, menu);
            }
        }
        return inject(handler, menu);
    }

	@Override
	public Set<Class<?>> getIgnoreClass() {
		return IGNORED;
	}

	@Override
	public boolean isIgnoreClass(Class<?> kind) {
		return IGNORED.contains(kind);
	}

	@Override
	public ViewInjector addIgnoreClass(Class<?> kind) {
		IGNORED.add(kind);
		return this;
	}

	@Override
	public ViewInjector removeIgnoreClass(Class<?> kind) {
		IGNORED.remove(kind);
		return this;
	}

    protected void injectContent(Activity activity) throws RuntimeException {
        ObjectUtils.requireNonNull(activity);
        ContentView contentView = findContentView(activity.getClass());
        if (contentView == null) return;
        int viewId = contentView.value();
        if (viewId > 0) {
            activity.setContentView(viewId);
        }
	}

    protected View injectContent(Object fragment, Class<?> handlerType, LayoutInflater inflater, ViewGroup container) {
        ObjectUtils.requireNonNull(fragment, handlerType, inflater);
        View view = null;
        ContentView contentView = findContentView(handlerType);
        if (contentView != null) {
            int viewId = contentView.value();
            if (viewId > 0) {
                try {
                    view = inflater.inflate(viewId, container, false);
                } catch (Throwable e) {
                    ThrowableUtils.throwRuntimeException("inflate failed!", e);
                }
            }
        }
        return view;
	}
    protected MenuManager inject(MenuManager manager, Object handler, Class<?> handlerType) {
        if (handlerType == null || IGNORED.contains(handlerType)) {
            return manager;
        }

        Method[] methods = getDeclaredMethods(handlerType);
        for (int i = 0; methods != null && i < methods.length; i++) {
            Method method = methods[i];
            method.setAccessible(true);

            MenuItemEvent event = method.getAnnotation(MenuItemEvent.class);
            if (event != null) {
                //仅接受空参数方法和只有一个参数的方法
                if ((method.getParameterTypes() == null ?0: method.getParameterTypes().length) > 0) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1 
                        || MenuItem.class != parameterTypes[0]) {
                        continue;
                    }
                }

                int[] values = new int[1 + event.values().length];
                merge: {
                    values[0] = event.value();
                    for (int j=0; j < event.values().length; j++) {
                        values[1 + j] = event.values()[j];
                    }
                }
                for (int itemId:values) {
                    if (itemId <= 0) continue;
                    MenuItem menuItem = manager.getMenu().findItem(itemId);
                    manager.addEventMethod(handler, method, menuItem, itemId);
                }
            }
        }

        return inject(manager, handler, handlerType.getSuperclass());
    }

	protected OptionsMenu findOptionsMenu(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        OptionsMenu optionsMenu = thisCls.getAnnotation(OptionsMenu.class);
        if (optionsMenu == null) {
            return findOptionsMenu(thisCls.getSuperclass());
        }
        return optionsMenu;
    }

    protected ContentView findContentView(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls) || thisCls.getName().startsWith("androidx.")) { 
            return null;
        }
        ContentView contentView = thisCls.getAnnotation(ContentView.class);
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    protected void injectObject(Object handler, Class<?> handlerType, ViewFinder finder,  InjectOptions options) {
        if (handlerType == null || IGNORED.contains(handlerType)) {
            return;
        }

        //循环反射父类
        injectObject(handler, handlerType.getSuperclass(), finder, options);

        if (options.isInjectField()) {
            injectField(handler, handlerType, finder);
        }

        if (options.isInjectMethod()) {
            injectMethod(handler, handlerType, finder);
        }

    }

    protected void injectField(Object handler, Class<?> handlerType, ViewFinder finder) {
        Field[] fields = getDeclaredFields(handlerType);
        for (int i = 0; fields != null && i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            injectField(field, handler, handlerType, finder);
        }
    }

	protected void injectField(Field field, Object handler, Class<?> handlerType, ViewFinder finder) {
		injectViewField(field, handler, handlerType, finder);
	}

    protected void injectViewField(Field field, Object handler, Class<?> handlerType, ViewFinder finder) throws RuntimeException {
        ViewInject viewInject = field.getAnnotation(ViewInject.class);
        if (viewInject == null) return;

        Class<?> fieldType = field.getType();
        if (Modifier.isStatic(field.getModifiers()) 
            ||  Modifier.isFinal(field.getModifiers())
            || fieldType.isPrimitive()
            || fieldType.isArray()) {
            throw new IllegalArgumentException("This field type is not supported @ViewInject for "
                                               + handlerType.getSimpleName() + "." + field.getName());
        }

        int id = viewInject.value();
        View view = finder.findViewById(id, viewInject.parentId());
        if (view == null) {
            String idResName = null;
            try {
                idResName = Util.getApp().getResources().getResourceName(id);
            } catch (Resources.NotFoundException ignored) {}
            throw new RuntimeException("View not found @ViewInject for "
                                       + handlerType.getSimpleName() + "." + field.getName() 
                                       + (!TextUtils.isEmpty(idResName) ?", idName=" + idResName: "")
                                       + ", id=0x" + Integer.toHexString(id));
        }
        try {
            field.set(handler, view);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected void injectMethod(Object handler, Class<?> handlerType, ViewFinder finder) {
        Method[] methods = getDeclaredMethods(handlerType);
        for (int i = 0; methods != null && i < methods.length; i++) {
            Method method = methods[i];
            method.setAccessible(true);
            injectMethod(method, handler, handlerType, finder);
        }
    }

	protected void injectMethod(Method method, Object handler, Class<?> handlerType, ViewFinder finder) {
		injectMethodEvent(method, handler, handlerType, finder);
	}

    protected void injectMethodEvent(Method method, Object handler, Class<?> handlerType, ViewFinder finder)  {
        Event event = method.getAnnotation(Event.class);
        if (event == null) return;

        // id参数
        int[] values = new int[1 + event.values().length];
        merge: {
            values[0] = event.value();
            for (int i = 0; i < event.values().length; i++) {
                values[1 + i] = event.values()[i];
            }
        }

        int[] parentIds = event.parentId();
        int parentIdsLen = parentIds == null ? 0 : parentIds.length;

        //循环所有id，生成ViewInfo并添加代理反射
        for (int i = 0; i < values.length; i++) {
            int value = values[i];
            if (value <= 0) {
				// throw new RuntimeException("Invalid id " + value);
				continue;
            }
            ViewInfo info = new ViewInfo();
            info.value = value;
            info.parentId = parentIdsLen > i ? parentIds[i] : 0;
            try {
                EventManager.addEventMethod(finder, info, event, handler, method);
            } catch (Throwable e) {
				ThrowableUtils.throwRuntimeException(e);
            }
        }
    }

    protected Field[] getDeclaredFields(Class<?> type) {
        try {
            return type.getDeclaredFields();
        } catch (Throwable ignored) {}
        return null;
    }

	protected Method[] getDeclaredMethods(Class<?> type) {
        try {
            return type.getDeclaredMethods();
        } catch (Throwable ignored) {}
        return null;
    }

}
