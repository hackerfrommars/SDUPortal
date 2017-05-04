package com.example.uuu9.finalproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.uuu9.finalproject.MainActivity.currJson;


/**
 * Created by hiyandao101 on 5/4/17.
 */

public class MyService extends Service {
    final String LOG_TAG = "myLogs";
    public static final String loginURL = "http://192.168.43.209:8000/api/get_grades/"; // changes depending on situation
    String p_id= "150107068";// -> this values comes from
    String p_pass = "carsscarova114114";// get name or smt else...
    int timeMin = 1;


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
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
                    sendRequest(p_id, p_pass);
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
        final RequestQueue reqQueue = Volley.newRequestQueue(this);

        Map<String, String> mParams = new HashMap<>();
        mParams.put("p_user", id.toString());
        mParams.put("p_pass", pass.toString());

        final StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                loginURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Parsing json object response
                // response will be a json object
                currJson = response.toString();
                Log.d("myLogs", currJson + " >>>>>>>");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("myLogs", "error: " + error);
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mParams = new HashMap<String, String>();
                mParams.put("p_user", id);
                mParams.put("p_pass", pass);
                return mParams;
            }
        }
                ;
        int socketTimeout = 20000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        reqQueue.add(jsonObjReq);
    }

    public void checkGrades(String newJson){

    }




}
