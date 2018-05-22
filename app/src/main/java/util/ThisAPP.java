package util;

import android.app.Application;

/**
 * Created by Kr on 2018/5/15.
 */

public class ThisAPP extends Application {
    private static String selfID="none";

    public static String getSelfID() {
        return selfID;
    }

    public static void setSelfID(String selfID) {
        ThisAPP.selfID = selfID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        selfID="none";
    }
}
