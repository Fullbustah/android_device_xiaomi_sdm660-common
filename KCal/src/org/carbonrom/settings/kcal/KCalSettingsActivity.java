package org.carbonrom.settings.kcal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class KCalSettingsActivity extends Activity {

    private KCalSettings mKCalSettingsFragment;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getApplicationContext();

        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment == null) {
            mKCalSettingsFragment = new KCalSettings();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, mKCalSettingsFragment)
                    .commit();
        } else {
            mKCalSettingsFragment = (KCalSettings) fragment;
        }
    }

    static Context getContext() {
        return mContext;
    }

    private void reset() {
        mKCalSettingsFragment.mSetOnBoot.setChecked(Utils.SETONBOOT_DEFAULT);
        mKCalSettingsFragment.mRed.setValue(Utils.RED_DEFAULT);
        mKCalSettingsFragment.mRed.refresh(Utils.RED_DEFAULT);
        mKCalSettingsFragment.mGreen.setValue(Utils.GREEN_DEFAULT);
        mKCalSettingsFragment.mGreen.refresh(Utils.GREEN_DEFAULT);
        mKCalSettingsFragment.mBlue.setValue(Utils.BLUE_DEFAULT);
        mKCalSettingsFragment.mBlue.refresh(Utils.BLUE_DEFAULT);
        mKCalSettingsFragment.mMin.setValue(Utils.MINIMUM_DEFAULT);
        mKCalSettingsFragment.mMin.refresh(Utils.MINIMUM_DEFAULT);
        mKCalSettingsFragment.mSaturation.setValue(Utils.SATURATION_DEFAULT);
        mKCalSettingsFragment.mSaturation.refresh(Utils.SATURATION_DEFAULT);
        mKCalSettingsFragment.mValue.setValue(Utils.VALUE_DEFAULT);
        mKCalSettingsFragment.mValue.refresh(Utils.VALUE_DEFAULT);
        mKCalSettingsFragment.mContrast.setValue(Utils.CONTRAST_DEFAULT);
        mKCalSettingsFragment.mContrast.refresh(Utils.CONTRAST_DEFAULT);
        mKCalSettingsFragment.mHue.setValue(Utils.HUE_DEFAULT);
        mKCalSettingsFragment.mHue.refresh(Utils.HUE_DEFAULT);

        if (!mKCalSettingsFragment.mEnabled.isChecked()) {
            mKCalSettingsFragment.mEnabled.setTitle(R.string.kcal_disabled);
            mKCalSettingsFragment.prefState(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((Activity) KCalSettingsActivity.getContext()).finish();
                return true;

            case R.id.action_reset:
                reset();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_reset, menu);
        return true;
    }
}
