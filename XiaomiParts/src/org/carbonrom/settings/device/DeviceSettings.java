/*
 * Copyright (C) 2018 The Mokee Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.carbonrom.settings.device;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private final String ENABLE_HAL3_KEY = "hal3";
    private final String ENABLE_EIS_KEY = "eis";
    private final String TORCH_BRIGHTNESS_KEY = "torch_brightness";
    private final String VIBRATION_STRENGTH_KEY = "vibration_strength";

    private final String HAL3_SYSTEM_PROPERTY = "persist.camera.HAL3.enabled";
    private final String EIS_SYSTEM_PROPERTY = "persist.camera.eis.enable";

    private final String TORCH_1_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_0/max_brightness";
    private final String TORCH_2_BRIGHTNESS_PATH = "/sys/devices/soc/800f000.qcom,spmi/spmi-0/spmi0-03/800f000.qcom,spmi:qcom,pm660l@3:qcom,leds@d300/leds/led:torch_1/max_brightness";
    private final String VIBRATION_STRENGTH_PATH = "/sys/devices/virtual/timed_output/vibrator/vtg_level";

    private final String KEY_CATEGORY_DISPLAY = "display";
    private final String KEY_DEVICE_DOZE = "device_doze";
    private final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.carbonrom.settings.doze";
    private final String KEY_DEVICE_KCAL = "device_kcal";
    private final String KEY_DEVICE_KCAL_PACKAGE_NAME = "org.carbonrom.settings.kcal";

    private SwitchPreference mEnableHAL3;
    private SwitchPreference mEnableEIS;
    private TorchSeekBarPreference mTorchBrightness;
    private VibrationSeekBarPreference mVibrationStrength;

    // value of vtg_min and vtg_max
    final int minVibration = 116;
    final int maxVibration = 3596;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);
        mEnableHAL3 = (SwitchPreference) findPreference(ENABLE_HAL3_KEY);
        mEnableHAL3.setChecked(FileUtils.getProp(HAL3_SYSTEM_PROPERTY, false));
        mEnableHAL3.setOnPreferenceChangeListener(this);

        mEnableEIS = (SwitchPreference) findPreference(ENABLE_EIS_KEY);
        mEnableEIS.setChecked(FileUtils.getProp(EIS_SYSTEM_PROPERTY, false));
        mEnableEIS.setOnPreferenceChangeListener(this);

        mTorchBrightness = (TorchSeekBarPreference) findPreference(TORCH_BRIGHTNESS_KEY);
        mTorchBrightness.setEnabled(FileUtils.fileWritable(TORCH_1_BRIGHTNESS_PATH) && FileUtils.fileWritable(TORCH_2_BRIGHTNESS_PATH));
        mTorchBrightness.setOnPreferenceChangeListener(this);

        mVibrationStrength = (VibrationSeekBarPreference) findPreference(VIBRATION_STRENGTH_KEY);
        mVibrationStrength.setEnabled(FileUtils.fileWritable(VIBRATION_STRENGTH_PATH));
        mVibrationStrength.setOnPreferenceChangeListener(this);

        PreferenceCategory displayCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_DISPLAY);
        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            displayCategory.removePreference(findPreference(KEY_DEVICE_DOZE));
        }

        if (!isAppInstalled(KEY_DEVICE_KCAL_PACKAGE_NAME)) {
            displayCategory.removePreference(findPreference(KEY_DEVICE_KCAL));
        }
    }


    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        if (ENABLE_EIS_KEY.equals(key)) {

            return true;
        }
        switch (key) {
            case ENABLE_HAL3_KEY:
                mEnableHAL3.setChecked((Boolean) value);
                FileUtils.setProp(HAL3_SYSTEM_PROPERTY, (Boolean) value);
                break;

            case ENABLE_EIS_KEY:
                mEnableEIS.setChecked((Boolean) value);
                FileUtils.setProp(EIS_SYSTEM_PROPERTY, (Boolean) value);
                break;

            case TORCH_BRIGHTNESS_KEY:
                mTorchBrightness.setValue((int) value);
                FileUtils.setValue(TORCH_1_BRIGHTNESS_PATH, (int) value);
                FileUtils.setValue(TORCH_2_BRIGHTNESS_PATH, (int) value);
                break;

            case VIBRATION_STRENGTH_KEY:
                double vibrationValue = (int) value / 100.0 * (maxVibration - minVibration) + minVibration;
                FileUtils.setValue(VIBRATION_STRENGTH_PATH, vibrationValue);
                break;

            default:
                break;
        }
        return true;
    }

    private boolean isAppInstalled(String uri) {
        PackageManager packageManager = DeviceSettingsActivity.getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // Throw it far away
        }
        return false;
    }

}
