package com.mobithink.carbon.event;

import android.util.Log;

/**
 * Created by mplaton on 13/02/2017.
 */

public class AppLog {
        private static final String APP_TAG = "AudioRecorder";

        public static int logString(String message) {
            return Log.i(APP_TAG, message);
        }
}
