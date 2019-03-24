package com.cyh.logincomponent;

import android.app.Application;

import com.cyh.componentlib.IAppComponent;
import com.cyh.componentlib.ServiceFactory;

public class LoginApp extends Application implements IAppComponent {
    private static Application application;
    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializa(this);
    }

    @Override
    public void initializa(Application app) {
        application = app;
        ServiceFactory.getInstance().setLoginService(new LoginService());
    }
}
