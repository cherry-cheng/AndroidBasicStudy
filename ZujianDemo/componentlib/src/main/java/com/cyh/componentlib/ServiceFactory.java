package com.cyh.componentlib;

import com.cyh.componentlib.impl.EmptyLoginService;
import com.cyh.componentlib.impl.EmptyMineService;
import com.cyh.componentlib.service.ILoginService;
import com.cyh.componentlib.service.IMineService;

public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();
    public static ServiceFactory getInstance() {
        return instance;
    }
    private ServiceFactory() {

    }

    private ILoginService mLoginService;
    private IMineService mMineService;
    public ILoginService getLoginService() {
        if (mLoginService == null) {
            mLoginService = new EmptyLoginService();
        }
        return mLoginService;
    }

    public void setLoginService(ILoginService loginService) {
        mLoginService = loginService;
    }

    public IMineService getMineService() {
        if (mMineService == null) {
            mMineService = new EmptyMineService();
        }
        return mMineService;
    }

    public void setMineService(IMineService mineService) {
        mMineService = mineService;
    }
}
