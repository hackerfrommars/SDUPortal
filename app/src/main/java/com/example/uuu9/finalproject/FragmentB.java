package com.example.uuu9.finalproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentB extends Fragment {
    View view;
    FragmentTransaction transaction;
    ListView lv;
    String[] list;
    JSONObject jsonParse,jsonInner;
    String len,name,mt1,mt2,fin,avg;
    final String LOG_TAG = "Dlogs";
    ArrayList<String> subjects;
    FragmentA a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.more_tab_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.change_user:
                SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("p_user", "");
                editor.putString("p_pass", "");
                editor.commit();
                transaction = getFragmentManager().beginTransaction();
                a = new FragmentA();
                transaction.replace(R.id.container, a);
                transaction.commit();

                Log.d("MenuItem", "1");
                break;
            case R.id.about:
                Log.d("MenuItem", "3");
                break;
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_b, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sPref", MODE_PRIVATE);
        if(((MainActivity)getActivity()).isMyServiceRunning(MyService.class)){
            Log.d("myLogs", "Service running ");
        }
        else{
            Log.d("myLogs", "Service is not running ");
            // MyService start should be here ... >>>
            ((MainActivity)getActivity()).onClickStop(view);
        }
        // for testing >>> then should be changed to start and should be placed inside else{}
        try {
            jsonParse = new JSONObject(sharedPref.getString("json", ""));
            len = jsonParse.getString("len");
            jsonInner = jsonParse.getJSONObject("1");
            name = jsonInner.getString("course_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }





//        Log.d(LOG_TAG, "len = "+len +"; courseName:" + name);
//        Log.d(LOG_TAG, "jSOn: " + jsonParse);



        ExpandableListView listView = (ExpandableListView)view.findViewById(R.id.exListView);

        //Создаем набор данных для адаптера
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();



        for(int i =1; i<Integer.parseInt(len)+1;i++){
            try {
                jsonInner = jsonParse.getJSONObject(i+"");
                mt1 = jsonInner.getString("mt1");
                mt2 = jsonInner.getString("mt2");
                fin = jsonInner.getString("fin");
                avg = jsonInner.getString("avg");

                ArrayList<String> children = new ArrayList<String>();
                children.add("Midterm1: "+mt1);
                children.add("Midterm2: "+mt2);
                children.add("Final: "+fin);
                children.add("Average: "+avg);
                groups.add(children);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(LOG_TAG, "name:"+name);
        }


        //Создаем адаптер и передаем context и список с данными
        ExpListAdapter adapter = new ExpListAdapter(getActivity().getApplicationContext(), groups);
        listView.setAdapter(adapter);


        return view;
    }
}
