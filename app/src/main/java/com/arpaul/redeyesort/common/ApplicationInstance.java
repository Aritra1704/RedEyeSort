package com.arpaul.redeyesort.common;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by ARPaul on 08-03-2017.
 */

public class ApplicationInstance extends MultiDexApplication {

    public static final int LOADER_FETCH_JSON_DATA      = 1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
