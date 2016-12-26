package com.hvstudy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Doctruyen extends AppCompatActivity{
    TextView tv ;
    Spinner sp;
    ImageView next;
    int MaxChuong,PresentChuong =1;

    //CỦA TUI. KHAI BÁO FONT
    Typeface times, calibri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctruyen);
        Intent intentGui = getIntent();
        Bundle duLieuNhan = intentGui.getBundleExtra("data");
        final int a=duLieuNhan.getInt("ID");
        tv = (TextView) findViewById(R.id.txtNoidung);
        sp = (Spinner) findViewById(R.id.spDanhsach);
        next = (ImageView) findViewById(R.id.btnNext);
        times = Typeface.createFromAsset(getAssets(), "typefaces/times.ttf");
        calibri = Typeface.createFromAsset(getAssets(), "typefaces/calibri.ttf");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Hiennoidung().execute("http://hvtekshop.com/searchid.php?idtr="+a+"&idch=1");
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Hiendanhsach().execute("http://hvtekshop.com/hiendanhsach.php?idtr="+a);
            }
        });
        // Bấm NEXT
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PresentChuong == MaxChuong)
                {
                    Toast.makeText(Doctruyen.this,"Bạn đang ở Chương cuối rồi!",Toast.LENGTH_SHORT).show();
                }
                else {
                    final int x = PresentChuong+1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Hiennoidung().execute("http://hvtekshop.com/searchid.php?idtr="+a+"&idch="+x);
                        }
                    });
                    sp.setSelection(PresentChuong);
                }
            }
        });
       sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                final int x = i+1;
                PresentChuong = x;
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       new Hiennoidung().execute("http://hvtekshop.com/searchid.php?idtr="+a+"&idch="+x);

                   }
               });
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        //CỦA TUI. LOAD SETTINGS VÀO
        loadSettings();
    }
    class Hiennoidung extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            // Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            String noidung = "";
            JSONArray mang = null;
            try {
                mang = new JSONArray(s);
                JSONObject truyen = mang.getJSONObject(0);
                noidung = truyen.getString("noidung");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tv.setText(noidung);
        }
    }

    // đọc nội dung JSON và hiện lên danh sách
    class Hiendanhsach extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();

           ArrayList<String> dschuong = new ArrayList<String>();
            JSONArray chuong = null;
            try {
                chuong = new JSONArray(s);
                MaxChuong = chuong.length();
                for(int i=0; i<chuong.length();i++)
                {
                    JSONObject truyen = chuong.getJSONObject(i);
                    dschuong.add(truyen.getString("tuadechuong"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ArrayAdapter arrAdapter = new ArrayAdapter(
                    Doctruyen.this,
                    R.layout.spinner_item,
                    dschuong
            );
            arrAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            sp.setAdapter(arrAdapter);
        }
    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();
        try
        {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    //CỦA TUI. TẠO HÀM LOAD SETTINGS.
    private void loadSettings() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {

            int posTypeface = sharedPreferences.getInt("Typeface", 0);
            switch (posTypeface) {
                case 1:
                    tv.setTypeface(calibri);
                    break;
                case 2:
                    tv.setTypeface(times);
                    break;
                default:
                    tv.setTypeface(Typeface.DEFAULT);
                    break;
            }

            int posSize = sharedPreferences.getInt("Size", 1);
            switch (posSize) {
                case 0:
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    break;
                case 2:
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    break;
                case 3:
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    break;
                default:
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            }

            int posStyle = sharedPreferences.getInt("Style", 0);
            switch (posStyle) {
                case 1:
                    tv.setTextColor(Color.rgb(30, 30, 30));
                    tv.setBackgroundColor(Color.rgb(255, 255, 255));
                    break;
                case 2:
                    tv.setTextColor(Color.rgb(30, 30, 30));
                    tv.setBackgroundColor(Color.rgb(226, 226, 226));
                    break;
                case 3:
                    tv.setTextColor(Color.rgb(237, 237, 237));
                    tv.setBackgroundColor(Color.BLACK);
                    break;
                default:
                    tv.setTextColor(Color.rgb(30, 30, 30));
                    tv.setBackgroundColor(Color.rgb(249, 249, 232));
                    break;
            }

        }
    }

}

