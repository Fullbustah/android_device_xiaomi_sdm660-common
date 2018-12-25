package org.carbonrom.settings.kcal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BootReceiver extends BroadcastReceiver implements Utils {
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPrefs.getBoolean(PREF_SETONBOOT, false)) {
            restoreValue(KCAL_ENABLE, sharedPrefs.getBoolean(PREF_ENABLED, false) ? "1" : "0");

            String rgbValue = sharedPrefs.getInt(PREF_RED, RED_DEFAULT) + " " +
                    sharedPrefs.getInt(PREF_GREEN, GREEN_DEFAULT) + " " +
                    sharedPrefs.getInt(PREF_BLUE, BLUE_DEFAULT);

            restoreValue(KCAL_RGB, rgbValue);
            restoreValue(KCAL_MIN, sharedPrefs.getString(PREF_MINIMUM, String.valueOf(MINIMUM_DEFAULT)));
            restoreValue(KCAL_SAT, sharedPrefs.getString(PREF_SATURATION, String.valueOf(SATURATION_DEFAULT)));
            restoreValue(KCAL_VAL, String.valueOf(sharedPrefs.getInt(PREF_VALUE, VALUE_DEFAULT) + VALUE_OFFSET));
            restoreValue(KCAL_CONT, String.valueOf(sharedPrefs.getInt(PREF_CONTRAST, CONTRAST_DEFAULT) + CONTRAST_OFFSET));
            restoreValue(KCAL_HUE, sharedPrefs.getString(PREF_HUE, String.valueOf(HUE_DEFAULT)));
        }
    }

    private void restoreValue(String path, String value) {
        if (path == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(value.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
