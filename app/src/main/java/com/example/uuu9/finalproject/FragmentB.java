package com.example.uuu9.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class FragmentB extends Fragment {
    View view;
    ListView lv;
    String[] list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_b, container, false);
        lv = (ListView)view.findViewById(R.id.listW);
        list = new String[]{"A", "B", "C"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.listitem,list);
        lv.setAdapter(arrayAdapter);

        ((MainActivity)getActivity()).onClickStop(view);// this should be handled well enough...
        return view;
    }
}
