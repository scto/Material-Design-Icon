package org.view;

public class InjectOptions {

    private boolean injectContentView  = true;

    private boolean injectField  = true;

    private boolean injectMethod  = true;

    public InjectOptions setInjectContentView(boolean injectContentView) {
        this.injectContentView = injectContentView;
        return this;
    }

    public boolean isInjectContentView() {
        return injectContentView;
    }

    public InjectOptions setInjectField(boolean injectField) {
        this.injectField = injectField;
        return this;
    }

    public boolean isInjectField() {
        return injectField;
    }

    public InjectOptions setInjectMethod(boolean injectMethod) {
        this.injectMethod = injectMethod;
        return this;
    }

    public boolean isInjectMethod() {
        return injectMethod;
    }
    
    public static InjectOptions newInstance(){
        return new InjectOptions();
    }

}
