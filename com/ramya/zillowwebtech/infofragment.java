package com.ramya.zillowwebtech;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class infofragment extends Fragment {

    public infofragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View tableView = inflater.inflate(R.layout.infofragment,
                container, false);
        
        return tableView;
    }
}
