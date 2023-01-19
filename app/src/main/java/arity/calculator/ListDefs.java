// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import arity.calculator.R;

public class ListDefs extends ListActivity {
    private Defs defs;
    private ArrayAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defs = Calculator.defs;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, defs.lines);        
        setListAdapter(adapter);
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
