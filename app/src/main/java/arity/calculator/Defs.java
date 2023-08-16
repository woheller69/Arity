// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import org.javia.arity.*;

class Defs extends FileHandler {
    private static final int SIZE_LIMIT = 50;
    ArrayList<String> lines = new ArrayList<String>();
    private Symbols symbols;

    Defs(Context context, Symbols symbols) {
	super(context, "defs", 1);
	this.symbols = symbols;
        symbols.pushFrame();
	load();
    }

    void clear() {
        lines.clear();
        symbols.popFrame();
        symbols.pushFrame();
    }

    int size() {
        return lines.size();
    }

    void doRead(DataInputStream is) throws IOException {
	int size = is.readInt();
	for (int i = 0; i < size; ++i) {
	    String line = is.readUTF();
	    lines.add(line);
	    try {
            FunctionAndName fan = symbols.compileWithName(line);
                symbols.define(fan);
                Function f = fan.function;
                int arity = f.arity();
                if (arity == 0) {
                    symbols.define(fan.name, f.evalComplex());
                }
	    } catch (SyntaxException e) {
		// ignore
	    }
	}
    }

    void doWrite(DataOutputStream os) throws IOException {
	os.writeInt(lines.size());
	for (String s : lines) {
	    os.writeUTF(s);
	}
    }

    void add(String text) {
	if (lines.size() >= SIZE_LIMIT) {
	    lines.remove(0);
	}
	lines.add(text);
    }
}
