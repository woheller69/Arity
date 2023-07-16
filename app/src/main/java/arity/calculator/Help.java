// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Help extends AppCompatActivity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.help);
        WebView view = findViewById(R.id.my_webview);
        if(WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                WebSettingsCompat.setAlgorithmicDarkeningAllowed(view.getSettings(), true);
            }
        }

        String filename = "help-en.html";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String language = null;
            language = getResources().getConfiguration().getLocales().get(0).getLanguage();
            filename = "help-"+language+".html";

            AssetManager am = getAssets();
            try {
                List<String> mapList = Arrays.asList(am.list("help"));

                if (!mapList.contains(filename)) {
                    filename = "help-en.html";
                }
            } catch ( IOException ex){
                ex.printStackTrace();
            }
        }

        view.loadUrl("file:///android_asset/"+ filename);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }   
}
