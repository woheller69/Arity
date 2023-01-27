// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Help extends AppCompatActivity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        WebView view = new WebView(this);
        setContentView(view);
        view.loadUrl("file:///android_asset/"+ getString(R.string.helpfile));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }   
}
