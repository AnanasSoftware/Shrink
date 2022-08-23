package com.ananas.shrink;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.ananas.shrink.utility.Utility;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_about);
        getSupportActionBar().getCustomView().findViewById(R.id.appBar_back).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.app_version)).setText(String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME));
        findViewById(R.id.ananasLogo).setOnClickListener(view -> Utility.openUrl(this, getString(R.string.facebook)));
    }
}
