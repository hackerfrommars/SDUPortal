package com.example.uuu9.finalproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;

import static android.R.attr.fragment;


public class FragmentA extends Fragment implements OnClickListener {
    FragmentTransaction transaction;
    View view;
    Button enter;
    EditText id,pass;
    FragmentB b;
    String identificator, password;
    private static final String TAG = "myLogsFragmentA";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a, container, false);
        id = (EditText) view.findViewById(R.id.idiwka);
        pass = (EditText) view.findViewById(R.id.pass);
        enter = (Button) view.findViewById(R.id.enter);
        enter.setOnClickListener(this);
        b = new FragmentB();
        return view;
    }
    public String getID(){
        return id.getText().toString();
    }

    public String getPass(){
        return pass.getText().toString();
    }


    @Override
    public void onClick(View v) {
        transaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.enter:
                transaction.replace(R.id.container, b);

                identificator = getID();
                password = getPass();

                Log.d(TAG, identificator+ ":" + password);

                break;
        }
        transaction.commit();
    }
}
