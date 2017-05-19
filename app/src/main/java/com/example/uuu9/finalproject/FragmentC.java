package com.example.uuu9.finalproject;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class FragmentC extends Fragment {

    View view;
    String p_user;
    String p_pass;
    String profileUrl = "http://192.168.43.209:8000/api/get_profile/";
    ImageView iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_c, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", MODE_PRIVATE);
        p_user = sharedPref.getString("p_user", p_user);
        p_pass = sharedPref.getString("p_pass", p_pass);
        Log.d("myLogs", p_user + " : " + p_pass);
        iv = (ImageView)view.findViewById(R.id.imageView);
        if(((MainActivity)getActivity()).isMyServiceRunning(MyService.class)){
            Log.d("myLogs", "Service running ");
        }
        else{
            Log.d("myLogs", "Service is not running ");
            // MyService start should be here ... >>>
            ((MainActivity)getActivity()).onClickStop(view);
        }

        if(sharedPref.contains("profile_json") && !sharedPref.getString("profile_json", "").equals("")){
            Log.d("myLogs", "have profile");

            // TODO do parsing ...

        }
        else{
            // send request volley to profile...
            sendRequestToProfile(p_user, p_pass);
        }

        File imgFile = new  File(getActivity().getExternalFilesDir(null) + "/portal/profile.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv.setImageBitmap(myBitmap);
            Log.d("myLogs", "file exist");
        }
        else{
            Log.d("myLogs", "file does not exist");
        }

        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return view;
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            File imgFile = new  File(getActivity().getExternalFilesDir(null) + "/portal/profile.jpg");
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv.setImageBitmap(myBitmap);
                Log.d("myLogs", "file exist");
            }
            else{
                Log.d("myLogs", "file does not exist");
            }
        }
    };

    public void someTask() {
        new Thread(new Runnable() {
            public void run() {
                // do smt...
            }
        }).start();
    }
    public void sendRequestToProfile(String p_user, String p_pass){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> mParams = new HashMap<>();
        mParams.put("p_user", p_user);
        mParams.put("p_pass", p_pass);
//        Log.d("myLogs", id + "");
//        Log.d("myLogs", pass + "");

        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, profileUrl, mParams, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("profile_json", response.toString());
                    editor.commit();
                    Log.d("myLogs", "profile_json saved");
                    Log.d("myLogs", "profile_json :   " + response.toString());

                    String img_url = null;
                    try {
                        img_url = (new JSONObject(sharedPref.getString("profile_json", ""))).getString("img_url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("myLogs", "url: " + img_url);
                    downloadFile(img_url);

                    File imgFile = new  File(getActivity().getExternalFilesDir(null) + "/portal/profile.jpg");
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        iv.setImageBitmap(myBitmap);
                        Log.d("myLogs", "file exists");
                    }
                    else{
                        Log.d("myLogs", "file does not exist");
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("myLogs", "error: " + error);
            }
        });
        int socketTimeout = 30000; // 20 sec
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }


    public void downloadFile(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/portal");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);
        Log.d("myLogs", "downloading image");
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Image download")
                .setDescription("Downloading profile image from SDU portal")
                .setDestinationInExternalFilesDir(getActivity(), "/portal", "profile.jpg");
        mgr.enqueue(request);


    }

}
