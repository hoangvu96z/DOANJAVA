package com.hvstudy.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    String[] typeface;
    String[] style;
    String[] size;
    Spinner spnTypeface;
    Spinner spnSize;
    Spinner spnStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        typeface = getResources().getStringArray(R.array.typeface_list);
        spnTypeface = (Spinner) findViewById(R.id.spnFont);
        ArrayAdapter<String> adapterTypeface = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeface);
        adapterTypeface.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnTypeface.setAdapter(adapterTypeface);

        size = getResources().getStringArray(R.array.size_list);
        spnSize = (Spinner) findViewById(R.id.spnSize);
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, size);
        adapterSize.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnSize.setAdapter(adapterSize);

        style = getResources().getStringArray(R.array.style_list);
        spnStyle = (Spinner) findViewById(R.id.spnStyle);
        ArrayAdapter<String> adapterStyle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, style);
        adapterStyle.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnStyle.setAdapter(adapterStyle);

        this.loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            int posTypeface = sharedPreferences.getInt("Typeface", 0);
            this.spnTypeface.setSelection(posTypeface);
            int posSize = sharedPreferences.getInt("Size", 1);
            this.spnSize.setSelection(posSize);
            int posStyle = sharedPreferences.getInt("Style", 0);
            this.spnStyle.setSelection(posStyle);
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Typeface", this.spnTypeface.getSelectedItemPosition());
        editor.putInt("Size", this.spnSize.getSelectedItemPosition());
        editor.putInt("Style", this.spnStyle.getSelectedItemPosition());
        editor.apply();
        finish();
    }

}
