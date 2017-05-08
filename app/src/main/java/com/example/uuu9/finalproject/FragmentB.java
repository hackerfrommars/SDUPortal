package com.example.uuu9.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
        if(((MainActivity)getActivity()).isMyServiceRunning(MyService.class)){
            Log.d("myLogs", "Service running ");
        }
        else{
            Log.d("myLogs", "Service is not running ");
            // MyService start should be here ... >>>
            ((MainActivity)getActivity()).onClickStop(view);
        }
        // for testing >>> then should be changed to start and should be placed inside else{}
        return view;
    }
}
