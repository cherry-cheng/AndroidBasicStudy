package com.cyh.componentlib.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public interface ILoginService {
    void launcher(Context context, String targetClass);
    Fragment newUserInfoFragment(FragmentManager fragmentManager, int viewId, Bundle bundle);
}
