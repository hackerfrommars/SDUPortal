package com.example.uuu9.finalproject;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogsMainActivity";
    String id;
    String pass;
    FragmentA a;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = new FragmentA();
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, a);
        transaction.commit();

//        id = a.getID();
//        pass = a.getPass();
//
//        Log.d(TAG, id+ ":" + pass);
    }
}
