package org.carbonrom.settings.kcal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;

public class KCalSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener, Utils {

    SwitchPreference mSetOnBoot;
    SwitchPreference mEnabled;

    CustomSeekBarPreference mRed;
    CustomSeekBarPreference mGreen;
    CustomSeekBarPreference mBlue;

    CustomSeekBarPreference mSaturation;
    CustomSeekBarPreference mValue;
    CustomSeekBarPreference mContrast;
    CustomSeekBarPreference mHue;
    CustomSeekBarPreference mMin;

    private FileUtils mFileUtils = new FileUtils();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(KCalSettingsActivity.getContext());

        boolean enabled = sharedPrefs.getBoolean(PREF_ENABLED, false);
        mSetOnBoot = (SwitchPreference) findPreference(PREF_SETONBOOT);
        //mSetOnBoot.setEnabled(sharedPrefs.getBoolean(PREF_SETONBOOT, false));
        mEnabled = (SwitchPreference) findPreference(PREF_ENABLED);
        mEnabled.setTitle(enabled ? R.string.kcal_enabled : R.string.kcal_disabled);
        //mSetOnBoot.setEnabled(enabled);
        mMin = (CustomSeekBarPreference) findPreference(PREF_MINIMUM);
        //mMin.setEnabled(enabled);
        mRed = (CustomSeekBarPreference) findPreference(PREF_RED);
        //mRed.setEnabled(enabled);
        mGreen = (CustomSeekBarPreference) findPreference(PREF_GREEN);
        //mGreen.setEnabled(enabled);
        mBlue = (CustomSeekBarPreference) findPreference(PREF_BLUE);
        //mBlue.setEnabled(enabled);
        mSaturation = (CustomSeekBarPreference) findPreference(PREF_SATURATION);
        //mSaturation.setEnabled(enabled);
        mValue = (CustomSeekBarPreference) findPreference(PREF_VALUE);
        //mValue.setEnabled(enabled);
        mContrast = (CustomSeekBarPreference) findPreference(PREF_CONTRAST);
        //mContrast.setEnabled(enabled);
        mHue = (CustomSeekBarPreference) findPreference(PREF_HUE);
        //mHue.setEnabled(enabled);

        prefState(enabled);

        mSetOnBoot.setOnPreferenceChangeListener(this);
        mEnabled.setOnPreferenceChangeListener(this);
        mMin.setOnPreferenceChangeListener(this);
        mRed.setOnPreferenceChangeListener(this);
        mGreen.setOnPreferenceChangeListener(this);
        mBlue.setOnPreferenceChangeListener(this);
        mSaturation.setOnPreferenceChangeListener(this);
        mValue.setOnPreferenceChangeListener(this);
        mContrast.setOnPreferenceChangeListener(this);
        mHue.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();

        switch (key) {
            case PREF_SETONBOOT:
                mSetOnBoot.setChecked((boolean) value);
                break;

            case PREF_ENABLED:
                setmEnabled((boolean) value);
                mEnabled.setChecked((boolean) value);
                break;

            case PREF_MINIMUM:
                setmMinimum((int) value);
                mMin.setValue((int) value);
                break;

            case PREF_RED:
                setmRed((int) value);
                mRed.setValue((int) value);
                break;

            case PREF_GREEN:
                setmGreen((int) value);
                mGreen.setValue((int) value);
                break;

            case PREF_BLUE:
                setmBlue((int) value);
                mBlue.setValue((int) value);
                break;

            case PREF_SATURATION:
                setmSaturation((int) value);
                mSaturation.setValue((int) value);
                break;

            case PREF_VALUE:
                setmValue((int) value);
                mValue.setValue((int) value);
                break;

            case PREF_CONTRAST:
                setmContrast((int) value);
                mContrast.setValue((int) value);
                break;

            case PREF_HUE:
                setmHue((int) value);
                mHue.setValue((int) value);
                break;

            default:
                break;
        }

        return true;
    }


    void prefState(boolean state) {
        mSetOnBoot.setEnabled(state);
        mRed.setEnabled(state);
        mGreen.setEnabled(state);
        mBlue.setEnabled(state);
        mMin.setEnabled(state);
        mSaturation.setEnabled(state);
        mValue.setEnabled(state);
        mContrast.setEnabled(state);
        mHue.setEnabled(state);
    }

    void setmEnabled(boolean value) {
        if (mFileUtils.isSupported(Utils.KCAL_ENABLE)) {
            mFileUtils.setValue(Utils.KCAL_ENABLE, value);
        }

        mEnabled.setTitle(value ? R.string.kcal_enabled : R.string.kcal_disabled);
        prefState(value);
    }

    void setmMinimum(int value) {
        if (mFileUtils.isSupported(Utils.KCAL_MIN)) {
            mFileUtils.setValue(Utils.KCAL_MIN, value);
        }
    }

    void setmRed(int value) {
        String rgbString = value + " " + mGreen.getValue() + " " + mBlue.getValue();
        if (mFileUtils.isSupported(Utils.KCAL_RGB)) {
            mFileUtils.setValue(Utils.KCAL_RGB, rgbString);
        }
    }

    void setmGreen(int value) {
        String rgbString = mRed.getValue() + " " + value + " " + mBlue.getValue();
        if (mFileUtils.isSupported(Utils.KCAL_RGB)) {
            mFileUtils.setValue(Utils.KCAL_RGB, rgbString);
        }
    }

    void setmBlue(int value) {
        String rgbString = mRed.getValue() + " " + mGreen.getValue() + " " + value;
        if (mFileUtils.isSupported(Utils.KCAL_RGB)) {
            mFileUtils.setValue(Utils.KCAL_RGB, rgbString);
        }
    }

    void setmSaturation(int value) {
        if (mFileUtils.isSupported(Utils.KCAL_SAT)) {
            mFileUtils.setValue(Utils.KCAL_SAT, value + SATURATION_OFFSET);
        }
    }

    void setmValue(int value) {
        if (mFileUtils.isSupported(Utils.KCAL_VAL)) {
            mFileUtils.setValue(Utils.KCAL_VAL, value + VALUE_OFFSET);
        }
    }

    void setmContrast(int value) {
        if (mFileUtils.isSupported(Utils.KCAL_CONT)) {
            mFileUtils.setValue(Utils.KCAL_CONT, value + CONTRAST_OFFSET);
        }
    }

    void setmHue(int value) {
        if (mFileUtils.isSupported(Utils.KCAL_HUE)) {
            mFileUtils.setValue(Utils.KCAL_HUE, value);
        }
    }
}

