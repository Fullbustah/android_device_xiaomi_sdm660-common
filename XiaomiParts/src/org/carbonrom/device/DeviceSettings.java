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
import android.os.SystemProperties;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private final String ENABLE_HAL3_KEY = "hal3";
    private final String ENABLE_EIS_KEY = "eis";
    private final String HAL3_SYSTEM_PROPERTY = "persist.camera.HAL3.enabled";
    private final String HAL1_SYSTEM_PROPERTY = "camera.hal1.packagelist";
    private final String EIS_SYSTEM_PROPERTY = "persist.camera.eis.enable";
    private final String HAL1_PACKAGELIST = "com.whatsapp";

    private final String KEY_CATEGORY_DISPLAY = "display";
    private final String KEY_DEVICE_DOZE = "device_doze";
    private final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.carbonrom.settings.doze";
    private final String KEY_DEVICE_KCAL = "device_kcal";
    private final String KEY_DEVICE_KCAL_PACKAGE_NAME = "org.carbonrom.settings.kcal";

    private SwitchPreference mEnableHAL3;
    private SwitchPreference mEnableEIS;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);
        mEnableHAL3 = (SwitchPreference) findPreference(ENABLE_HAL3_KEY);
        mEnableHAL3.setChecked(SystemProperties.getBoolean(HAL3_SYSTEM_PROPERTY, false));
        mEnableHAL3.setOnPreferenceChangeListener(this);

        mEnableEIS = (SwitchPreference) findPreference(ENABLE_EIS_KEY);
        mEnableEIS.setChecked(SystemProperties.getBoolean(EIS_SYSTEM_PROPERTY, false));
        mEnableEIS.setOnPreferenceChangeListener(this);

        PreferenceCategory displayCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_DISPLAY);
        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            displayCategory.removePreference(findPreference(KEY_DEVICE_DOZE));
        }

        if (!isAppInstalled(KEY_DEVICE_KCAL_PACKAGE_NAME)) {
            displayCategory.removePreference(findPreference(KEY_DEVICE_KCAL));
        }
    }

    private void setProp(String prop, boolean value) {
        if (value) {
            SystemProperties.set(prop, "1");
        } else {
            SystemProperties.set(prop, "0");
        }
    }

    private void setProp(String prop, String value) {
        SystemProperties.set(prop, value);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        if (ENABLE_HAL3_KEY.equals(key)) {
            mEnableHAL3.setChecked((Boolean) value);
            setProp(HAL1_SYSTEM_PROPERTY, (Boolean) value ? HAL1_PACKAGELIST : "");
            setProp(HAL3_SYSTEM_PROPERTY, (Boolean) value);
            return true;
        }
        if (ENABLE_EIS_KEY.equals(key)) {
            mEnableEIS.setChecked((Boolean) value);
            setProp(EIS_SYSTEM_PROPERTY, (Boolean) value);
            return true;
        }
        return true;
    }

    private boolean isAppInstalled(String uri) {
        PackageManager packageManager = DeviceSettingsActivity.getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

}
