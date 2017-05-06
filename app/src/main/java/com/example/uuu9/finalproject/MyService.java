package com.example.uuu9.finalproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



/**
 * Created by hiyandao101 on 5/4/17.
 */

public class MyService extends Service {
    final String LOG_TAG = "myLogs";
    public static final String loginURL = "http://192.168.43.209:8000/api/get_grades/"; // changes depending on situation
    String p_user= "150107068";// -> this values comes from
    String p_pass = "carsscarova114114";// get name or smt else...
    int timeMin = 1;
    Context context;


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        context = getApplicationContext();
        SharedPreferences sharedPref = getSharedPreferences("sPref", MODE_PRIVATE);
        p_user = sharedPref.getString("p_user", p_user);
        p_pass = sharedPref.getString("p_pass", p_pass);
        Log.d("myLogs", p_user + " : " + p_pass);
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void someTask() {
        new Thread(new Runnable() {
            public void run() {
                while(true){
                    sendRequest(p_user, p_pass);
                    try {
                        TimeUnit.MINUTES.sleep(timeMin);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void sendRequest(final String id, final String pass){
//        final RequestQueue reqQueue = Volley.newRequestQueue(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> mParams = new HashMap<>();
        mParams.put("p_user", id.toString());
        mParams.put("p_pass", pass.toString());
        Log.d("myLogs", id + "");
        Log.d("myLogs", pass + "");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, loginURL, mParams, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences sharedPref = getSharedPreferences("sPref", MODE_PRIVATE);
                if(!sharedPref.contains("json")){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("json", response.toString());
                    editor.commit();
                    Log.d("myLogs", "json saved");
                }
                else{
                    try {
                        checkGrades(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("myLogs", "JSON exeption");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("myLogs", "error: " + error);
            }
        });
        int socketTimeout = 20000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }
    public void checkGrades(JSONObject newGr) throws JSONException {
        boolean isEqual = true;
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences sharedPref = getSharedPreferences("sPref", MODE_PRIVATE);
        Log.d("myLogs", "saved Json in str form" + sharedPref.getString("json", ""));
        JSONObject oldGr = new JSONObject(sharedPref.getString("json", ""));
        Log.d("myLogs", " old from grade" + oldGr.toString());

        if(Integer.parseInt(oldGr.getString("len")) != Integer.parseInt(newGr.getString("len"))){
            // notification :::> grades and lessons changed
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("json", newGr.toString());
            editor.commit();
        }
        else{
            for(int i = 1; i <= Integer.parseInt(oldGr.getString("len")); i++){
                JSONObject old_course = oldGr.getJSONObject(i + "");
                JSONObject new_course = newGr.getJSONObject(i + "");
                String [] tags = new String[]{"course_name", "mt1", "mt2", "fin", "avg"};
                for(int j = 0; j < 5; j++){
                    if(!old_course.getString(tags[j]).equals(new_course.getString(tags[j]))){
                        isEqual = false;
                    }
                }
            }
            if(isEqual == false){
                // notification ----> smt changed
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("json", newGr.toString());
                editor.commit();
            }
            else{
                Log.d("myLogs", "nothing changed");
            }
        }
    }
}
