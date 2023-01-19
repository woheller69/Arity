package arity.calculator;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import arity.calculator.R;

public class Settings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        addPreferencesFromResource(R.xml.settings);
    }
}
