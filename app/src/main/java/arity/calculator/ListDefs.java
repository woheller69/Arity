// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsetsController;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ListDefs extends AppCompatActivity {
    private Defs defs;
    private ArrayAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listdefs);
        defs = Calculator.defs;
        ListView mListView = (ListView)findViewById(R.id.my_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defs.lines);        
        mListView.setAdapter(adapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isDarkMode = (nightModeFlags == Configuration.UI_MODE_NIGHT_YES);
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                if (isDarkMode) {
                    // Dark mode: remove light status bar appearance (use light icons)
                    insetsController.setSystemBarsAppearance(
                            0,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
                } else {
                    // Light mode: enable light status bar appearance (dark icons)
                    insetsController.setSystemBarsAppearance(
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
	MenuInflater inflater = new MenuInflater(this);
	inflater.inflate(R.menu.defs, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.clear_defs).setEnabled(defs.size() > 0);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_defs){
            defs.clear();
            defs.save();
            adapter.notifyDataSetInvalidated();
        } else {
            return false;
        }
        return true;
    }

}
