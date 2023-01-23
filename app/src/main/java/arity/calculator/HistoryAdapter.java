// Copyright (C) 2009 Mihai Preda

package arity.calculator;

import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;


class HistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private History history;

    static class TagData {
        TextView input;
        TextView result;
        TextView separator;
    }

    HistoryAdapter(Context context, History history) {
        this.history = history;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return history.entries.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int pos, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.history_line, parent, false);
            TagData tag = new TagData();
            tag.input = (TextView) view.findViewById(R.id.input);
            tag.result = (TextView) view.findViewById(R.id.result);
            tag.separator = (TextView) view.findViewById(R.id.equals_separator);
            view.setTag(tag);
        }
        TagData tag = (TagData) view.getTag();
        int revPos = history.entries.size() - pos - 1;
        HistoryEntry entry = history.entries.get(revPos);
        tag.input.setText(entry.line);
        String result = entry.result;
        tag.result.setText(result);
        tag.separator.setVisibility(result.length() == 0 ? View.GONE : View.VISIBLE);
        return view;
    }
}
