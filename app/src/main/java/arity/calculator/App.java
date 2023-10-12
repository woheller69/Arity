package arity.calculator;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

  }
}
