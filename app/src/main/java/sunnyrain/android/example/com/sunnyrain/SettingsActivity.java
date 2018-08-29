package sunnyrain.android.example.com.sunnyrain;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.prefs.Preferences;

import static android.R.attr.value;
import static android.R.id.list;

public class SettingsActivity extends PreferenceActivity
        implements  Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_unit_key)));
    }
    private  void bindPreferenceSummaryToValue(Preference preference){
    preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();
        if (preference instanceof ListPreference){
            ListPreference listPreference =(ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex > 0){
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }else {
                preference.setSummary(stringValue);
            }
        }
        return  true;
    }
}
