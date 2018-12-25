package org.carbonrom.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BootReceiver extends BroadcastReceiver {

    private final String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_0/max_brightness";
    private final String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_1/max_brightness";
    private final String VIBRATION_STRENGTH_PATH = "/sys/devices/virtual/timed_output/vibrator/vtg_level";

    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        restoreValue(TORCH_1_BRIGHTNESS_PATH, String.valueOf(sharedPrefs.getInt("torch_brightness", 100)));
        restoreValue(TORCH_2_BRIGHTNESS_PATH, String.valueOf(sharedPrefs.getInt("torch_brightness", 100)));
        restoreValue(VIBRATION_STRENGTH_PATH, String.valueOf(sharedPrefs.getInt("vibration_strength", 2796)));
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
