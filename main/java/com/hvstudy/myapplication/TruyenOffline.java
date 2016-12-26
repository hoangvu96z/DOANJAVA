package com.hvstudy.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class TruyenOffline extends AppCompatActivity {
    ListView story ;
    public  ArrayList<NoteTruyenOffline> noteList = new ArrayList<NoteTruyenOffline>();
    public   ArrayList<String> strings = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truyen_offline);
        story = (ListView) findViewById(R.id.lsvMain_offline);
        SQLite db = new SQLite(getApplicationContext(),"Manager_truyen_offline",null,1);
        List<NoteTruyenOffline> note = new ArrayList<NoteTruyenOffline>();
        note = db.getNoteByUser(MainActivity.username);
        ArrayList<String> str = new ArrayList<String>();
        for(int i = 0 ; i<note.size();i++)
        {
            str.add( note.get(i).getTuadetruyen());
        }
        ArrayAdapter adapter = new ArrayAdapter(TruyenOffline.this,android.R.layout.simple_list_item_1,str);
        story.setAdapter(adapter);
    }

    class DownloadOffline extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return MainActivity.docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONArray truyen ;
            truyen = null;
           try {
                truyen = new JSONArray(s);
                  } catch (JSONException e) {
                  e.printStackTrace();
                  }


                for(int i=0; i<truyen.length();i++)
                {
                    try {
                        JSONObject temp = truyen.getJSONObject(i);
                        noteList.add(new NoteTruyenOffline(MainActivity.username,
                                parseInt(temp.getString("idtruyen")),
                                parseInt(temp.getString("idchuong")),
                                temp.getString("tuadetruyen"),
                                temp.getString("tuadechuong"),
                                temp.getString("noidung")));
                        Toast.makeText(TruyenOffline.this,temp.getString("tuadetruyen"),Toast.LENGTH_SHORT).show();
                        strings.add(temp.getString("tuadechuong"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(TruyenOffline.this,android.R.layout.simple_list_item_1,strings);
                story.setAdapter(adapter);


        }
    }
}
