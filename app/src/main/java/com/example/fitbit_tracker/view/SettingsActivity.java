package com.example.fitbit_tracker.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.fitbit_tracker.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        getSupportFragmentManager().beginTransaction().replace(R.id.settingsFrameLayout, new SettingsFragment()).commit();
    }

    public static PreferenceManager preferenceManager;

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);

            preferenceManager = getPreferenceManager();

            final SwitchPreference autoTurnOnTVSwitch = (SwitchPreference) getPreferenceManager().findPreference("prefSampling");
            final EditTextPreference samplingThresholdPreference = (EditTextPreference) getPreferenceManager().findPreference("prefSamplingThreshold");

            if (autoTurnOnTVSwitch != null) {
                autoTurnOnTVSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean switchState = (boolean) newValue;
                        FragmentActivity activity = getActivity();

                        if (activity != null) {
                            SharedPreferences.Editor sp = activity.getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit();
                            sp.putBoolean("tvAutoOn",switchState).apply();
                        }
                        return true;
                    }
                });
            }

            if (samplingThresholdPreference != null) {
                samplingThresholdPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        editText.setSelection(0,editText.getText().length());
                    }
                });
                samplingThresholdPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        return true;
                    }
                });
            }

        }

    }

}