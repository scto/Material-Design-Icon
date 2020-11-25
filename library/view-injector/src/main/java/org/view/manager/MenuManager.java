package org.view.manager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.view.common.util.LogUtil;
import org.view.common.util.ObjectUtils;
import org.view.common.util.Util;

public class MenuManager  {

    private final Menu menu;

    private List<MenuItemClickEvent> mMenuItemClickListeners = new ArrayList<>();

    public MenuManager(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void addEventMethod(Object handler, Method method, MenuItem item) {
        addEventMethod(handler, method, item, item.getItemId());
    }

    public void addEventMethod(Object handler, Method method, MenuItem item, int itemId) {
        MenuItemClickEvent event = new MenuItemClickEvent(handler, method, item, itemId);
        if (item != null) {
            event.isBinded = true;
            item.setOnMenuItemClickListener(event);
        }
        /* if (mMenuItemClickListeners.contains(itemId)) {
         mMenuItemClickListeners.remove(itemId);
         }*/
        mMenuItemClickListeners.add(event);
    }

    public boolean onMenuItemSelected(MenuItem item) {
        ObjectUtils.requireNonNull(item);
        for (MenuItemClickEvent event : mMenuItemClickListeners) {
            if (item.equals(event.item) || event.itemId == item.getItemId()) {
                return event.onMenuItemClick(item);
            }
        }
        return false;
    }

    public boolean isBinded(MenuItem item) {
        for (MenuItemClickEvent event : mMenuItemClickListeners) {
            if (item.equals(event.item) || event.itemId == item.getItemId()) {
                return event.isBinded;
            }
        }
        return false;
    }

    private static class MenuItemClickEvent implements MenuItem.OnMenuItemClickListener {

        private static Toast sToast;

        private final Object handler;

        private final Method method;

        private final MenuItem item;

        private final int itemId;

        private boolean isBinded;

        public MenuItemClickEvent(Object handler, Method method, MenuItem item, int itemId) {
            this.handler = handler;
            this.method = method;
            this.item = item;
            this.itemId = itemId;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Object returnValue = null;
            try {
                int parameterCount = method.getParameterTypes() == null ?0: method.getParameterTypes().length;
                if (parameterCount == 0) {
                    returnValue = method.invoke(handler);
                } else if (parameterCount == 1) {
                    if (MenuItem.class == method.getParameterTypes()[0]) {
                        returnValue = method.invoke(handler, item);
                    } 
                }
            } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
                LogUtil.e(e.getMessage(), e);
                if (sToast != null) {
                    sToast.cancel();
                }
                sToast = Toast.makeText(Util.getApp(), e.toString(), Toast.LENGTH_SHORT);
                sToast.show();
            }
            if (returnValue != null && Boolean.class == returnValue.getClass()) {
                return ((Boolean)returnValue).booleanValue();
            }
            return false;
        }

    }

}
