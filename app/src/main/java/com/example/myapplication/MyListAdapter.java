package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter extends BaseAdapter {
    ArrayList<object> list;
    LayoutInflater inflater;
        public MyListAdapter(ArrayList<object> list, LayoutInflater inflater) {
            this.list = list;
            this.inflater = inflater;
        }

        public int getCount() {
            return list.size();}

        public Object getItem(int position) {
            return position;}

        public long getItemId(int position) {
            return position;}

        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            if (newView == null) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);
            }
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText(list.get(position).title);

            return newView;
        }
}




