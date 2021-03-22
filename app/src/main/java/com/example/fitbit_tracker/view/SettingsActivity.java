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

            final SwitchPreference samplingSwitchPreference = (SwitchPreference) getPreferenceManager().findPreference("prefSampling");
            final EditTextPreference samplingThresholdPreference = (EditTextPreference) getPreferenceManager().findPreference("prefSamplingThreshold");

            boolean thresholdPreferenceEnabled = getActivity().getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("prefSampling", true);

            if (samplingSwitchPreference != null) {
                samplingSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean switchState = (boolean) newValue;

                    samplingThresholdPreference.setEnabled(switchState);

                    FragmentActivity activity = getActivity();

                    if (activity != null) {
                        SharedPreferences.Editor sp = activity.getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit();
                        sp.putBoolean("prefSampling", switchState).apply();
                    }
                    return true;
                });
            }

            if (samplingThresholdPreference != null) {
                samplingThresholdPreference.setEnabled(thresholdPreferenceEnabled);

                samplingThresholdPreference.setOnBindEditTextListener(editText -> {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    editText.setSelection(0,editText.getText().length());
                });

                samplingThresholdPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    FragmentActivity activity = getActivity();

                    if (activity != null) {
                        SharedPreferences.Editor sp = activity.getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit();
                        int threshold = Integer.parseInt((String) newValue);
                        sp.putInt("prefSamplingThreshold", threshold).apply();
                    }
                    return true;
                });
            }

        }

    }

}