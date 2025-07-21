// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.javia.arity.Function;
import java.util.ArrayList;

public class ShowGraph extends AppCompatActivity {
    private Grapher view;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        ArrayList<Function> funcs = Calculator.graphedFunction;
        if (funcs == null) {
            finish();
            return;
        }
        int size = funcs.size();
        if (size == 1) {
            Function f = funcs.get(0);
            view = f.arity() == 1 ? new GraphView(this) : new Graph3dView(this);
            view.setFunction(f);
        } else {
            view = new GraphView(this);
            ((GraphView) view).setFunctions(funcs);
        }

        setContentView(R.layout.activity_graph);
        ViewGroup container = findViewById(R.id.container);
        container.addView((View) view);

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

    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        (new MenuInflater(this)).inflate(R.menu.graph, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.capture_screenshot ){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);
            }
            String fileName = view.captureScreenshot();
            if (fileName != null) {
                Toast.makeText(this, getString(R.string.screenshot_saved)+"\n" + fileName, Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.set_center){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_set_center));

            final EditText centerX = new EditText(this);
            centerX.setHint("X");
            final EditText centerY = new EditText(this);
            centerY.setHint("Y");

            centerX.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
            centerY.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);

            LinearLayout lay = new LinearLayout(this);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.addView(centerX);
            lay.addView(centerY);
            builder.setView(lay);

            builder.setPositiveButton(getString(android.R.string.ok), (dialog, whichButton) -> {
                float cx = 0;
                float cy = 0;
                if (!centerX.getText().toString().equals("")) cx = Float.parseFloat(centerX.getText().toString());
                if (!centerY.getText().toString().equals("")) cy = Float.parseFloat(centerY.getText().toString());
                if (view.getClass().equals(Graph3dView.class)){
                    Graph3d.setCenterX(cx);
                    Graph3d.setCenterY(cy);
                } else {
                    GraphView.setCenterX(cx);
                    GraphView.setCenterY(cy);
                }
                view.setDirty(true);
            });

            builder.setNegativeButton(getString(android.R.string.cancel), (dialog, whichButton) -> dialog.cancel());
            builder.show();
        } else {
            return false;
        }

        return true;
    }
}
