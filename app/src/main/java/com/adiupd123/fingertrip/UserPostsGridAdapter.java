package com.adiupd123.fingertrip;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserPostsGridAdapter extends BaseAdapter {

    ArrayList<HashMap<String, Object>> posts;

    public UserPostsGridAdapter(ArrayList<HashMap<String, Object>> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
