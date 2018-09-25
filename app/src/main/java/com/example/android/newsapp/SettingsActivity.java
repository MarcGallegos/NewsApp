package com.example.android.newsapp;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsEventPreferenceFrag extends PreferenceFragment{
//    implements Preference.OnPreferenceChangeListener{

        @Override
                public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

//            Preference orderBy=findPreference(getString(R.string.settings_order_by_key));
//            bindPreferenceSummaryToValue(orderBy);
//
//          }
        }
    }
}
