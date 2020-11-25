#dialog 不要用这个
-keep class androidx.appcompat.app.AppCompatDialog{*;}
-keep class androidx.appcompat.app.AppCompatDialog$1{*;}

-keep class androidx.appcompat.app.AppCompatCallback{*;}

-keep class androidx.annotation.* {*;}
-keep class androidx.core.view.KeyEventDispatcher{*;}
-keep class androidx.appcompat.view.ActionMode{*;}
-keep class androidx.appcompat.view.ActionMode$Callback{*;}

#Event
-keepclassmembers class * {
    @org.view.annotation.Event <methods>;
}
-keepclassmembers class * {
    @org.view.annotation.MenuItemEvent <methods>;
}

