package com.example.uuu9.finalproject;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogsMainActivity";
    String id;
    String pass;
    FragmentA a;
    FragmentTransaction transaction;
    public static String currJson = "";



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

    public void onClickStart(View v) {
        startService(new Intent(this, MyService.class));
    }

    public void onClickStop(View v) {
        stopService(new Intent(this, MyService.class));
    }
}
