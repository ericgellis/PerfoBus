package com.mobithink.carbon;

import android.app.Application;

import com.mobithink.carbon.managers.CarbonApplicationManager;

/**
 * Created by jpaput on 06/02/2017.
 */

public class CarbonApplication extends Application {

    // Singleton instance
    private static CarbonApplication mInstance = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;

        CarbonApplicationManager.getInstance().init();

    }

    public static CarbonApplication getInstance() {
        return mInstance ;
    }

    @Override
    public void onTerminate() {
        CarbonApplicationManager.getInstance().clearApp();
        super.onTerminate();
    }
}
