// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.os.Bundle;
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
        switch (id) {
        case R.id.clear_defs:
            defs.clear();
            defs.save();
            adapter.notifyDataSetInvalidated();
            break;
            
        default:
            return false;
        }
        return true;
    }

}
