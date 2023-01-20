package arity.calculator;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

import arity.calculator.R;

public class Settings extends PreferenceActivity {
    private AppCompatDelegate mDelegate;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        addPreferencesFromResource(R.xml.settings);
        ActionBar actionBar = getDelegate().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}
