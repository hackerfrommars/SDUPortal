package com.example.uuu9.finalproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", Context.MODE_PRIVATE);
        if(sharedPref.contains("p_user") && !sharedPref.getString("p_user", "").equals("") && sharedPref.contains("p_pass") && !sharedPref.getString("p_pass", "").equals("")){
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, b);
            transaction.commit();
        }


        return view;
    }
    public String getID(){
        return id.getText().toString(); // save shared pref as p_user as String...
    }

    public String getPass(){
        return pass.getText().toString(); // save shared pref as p_pass as String
    }


    @Override
    public void onClick(View v) {
        transaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.enter:
                transaction.replace(R.id.container, b);
                SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("p_user", id.getText().toString());
                editor.putString("p_pass", pass.getText().toString());
                editor.commit();
//                identificator = getID();
//                password = getPass();

//                Log.d(TAG, identificator+ ":" + password);

                break;
        }
        transaction.commit();
    }
}
