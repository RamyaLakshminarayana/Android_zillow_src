package com.ramya.zillowwebtech;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class zestimatefragment extends Fragment {

	public zestimatefragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View zestView = inflater.inflate(R.layout.zestimatefragment, container,
				false);
		return zestView;
	}
}