package com.scroll.ranger;

import android.app.Application;

/**
 * Created by fcl on 2017/5/22.
 * description:
 */

public class GlobalApplication extends Application {

    public static GlobalApplication globalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
    }
}
