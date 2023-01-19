// Copyright (C) 2009-2010 Mihai Preda

package arity.calculator;

import org.javia.arity.Function;

public interface Grapher {
    static final String SCREENSHOT_DIR = "/screenshots";
    public void setFunction(Function f);
    public void onPause();
    public void onResume();
    public String captureScreenshot();
}
