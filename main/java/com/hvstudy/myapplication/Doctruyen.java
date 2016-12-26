package com.hvstudy.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
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

import static java.lang.Integer.parseInt;

public class Doctruyen extends AppCompatActivity {
    TextView tv, textLoadSave;
    Spinner sp;
    ImageView next,setting,favorite,download;
    int MaxChuong,PresentChuong =1,isFavorited = 0;
    Typeface times, calibri;
    public static int scrollX = 0;
    public static int scrollY = -1;
    ScrollView scrollView;
    public  ArrayList<NoteTruyenOffline> noteList = new ArrayList<NoteTruyenOffline>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctruyen);
        Intent intentGui = getIntent();
        Bundle duLieuNhan = intentGui.getBundleExtra("data");
        textLoadSave = (TextView) findViewById(R.id.textLoadSave);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        final int a=duLieuNhan.getInt("ID");
        download = (ImageView) findViewById(R.id.imgDownload);
        tv = (TextView) findViewById(R.id.txtNoidung);
        sp = (Spinner) findViewById(R.id.spDanhsach);
        next = (ImageView) findViewById(R.id.imgNext);
        favorite = (ImageView) findViewById(R.id.imgFavorite);
        times = Typeface.createFromAsset(getAssets(), "typefaces/times.ttf");
        calibri = Typeface.createFromAsset(getAssets(), "typefaces/calibri.ttf");
        setting = (ImageView) findViewById(R.id.imageSettings);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Kiemtrayeuthich().execute("http://hvtekshop.com/checkfavorite.php?usr="+MainActivity.username+"&id="+a);
            }
        });
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
        //Bấm nút Setting
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        //Bấm nút Download
        download.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Doctruyen.this,TruyenOffline.class);
                startActivity(intent);
                return false;
            }
        });
        //Giữ nút Download
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Doctruyen.this,getString(R.string.download_beta),Toast.LENGTH_SHORT).show();
                SQLite db = new SQLite(getApplicationContext(),"Manager_truyen_offline",null,1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DownloadOffline().execute("http://hvtekshop.com/download.php?idtr="+a);
                    }
                });

                for (int x = 0;x<noteList.size();x++)
                {
                    db.addNote(noteList.get(x));
                }
            }
        });
        //Bấm nút Next
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PresentChuong == MaxChuong)
                {
                    Toast.makeText(Doctruyen.this,getString(R.string.read_last_character),Toast.LENGTH_SHORT).show();
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
                    scrollView.scrollTo(0,0);
                }
            }
        });
        // Bấm nút yêu thích
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.isLogined == 0)
                {
                    Toast.makeText(Doctruyen.this,getString(R.string.user_not_login_toast),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (isFavorited == 0) {
                        MainActivity.docNoiDung_Tu_URL("http://hvtekshop.com/addfavorite.php?usr=" + MainActivity.username + "&id=" + a);
                        Toast.makeText(Doctruyen.this, getString(R.string.favorite_add), Toast.LENGTH_SHORT).show();
                        isFavorited = 1;
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.like));
                    } else if (isFavorited == 1) {
                        MainActivity.docNoiDung_Tu_URL("http://hvtekshop.com/deletefavorite.php?usr=" + MainActivity.username + "&id=" + a);
                        isFavorited = 0;
                        favorite.setImageDrawable(getResources().getDrawable(R.drawable.unlike));
                        Toast.makeText(Doctruyen.this, getString(R.string.favorite_del), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //NÚT LOADSAVE
         setTextLoadSave();
        textLoadSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("State", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int load = sharedPreferences.getInt("load", 0);
                int idtr = sharedPreferences.getInt("idtr", 0);
                final int present = sharedPreferences.getInt("present", 0);
                if (load == 0) {
                    if (idtr != a) {
                        Toast.makeText(getApplicationContext(), "Load không nằm ở truyện này!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        CharSequence s = "Save";
                        textLoadSave.setText(s);
                        int x = sharedPreferences.getInt("scrollX", 0);
                        int y = sharedPreferences.getInt("scrollY", -1);
                        editor.putInt("load", 1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Hiennoidung().execute("http://hvtekshop.com/searchid.php?idtr="+a+"&idch="+present);
                            }
                        });
                        sp.setSelection(present);
                        scrollView.scrollTo(x, y);
                        Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    CharSequence l = "Load";
                    textLoadSave.setText(l);
                    scrollX = scrollView.getScrollX();
                    scrollY = scrollView.getScrollY();
                    editor.putInt("scrollX", scrollX);
                    editor.putInt("scrollY", scrollY);
                    editor.putInt("present", sp.getSelectedItemPosition());
                    editor.putInt("idtr", a);
                    editor.putInt("load", 0);
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });
        //XỬ LÝ KHI BẤM LÊN SPANNER
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

        loadSettings();
    }
    class Hiennoidung extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return MainActivity.docNoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
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
            return MainActivity.docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class Kiemtrayeuthich extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return MainActivity.docNoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            String ketqua = "";
            super.onPostExecute(s);
            // Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            JSONArray mang = null;
            try {
                mang = new JSONArray(s);
                JSONObject truyen = mang.getJSONObject(0);
                ketqua = truyen.getString("ketqua");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(ketqua.equals("true"))
            {
                isFavorited = 1;
                favorite.setImageDrawable(getResources().getDrawable(R.drawable.like));
            }
            else if (ketqua.equals("false"))
            {
                isFavorited = 0;
                favorite.setImageDrawable(getResources().getDrawable(R.drawable.unlike));
            }

        }
    }
    private void setTextLoadSave() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("State", Context.MODE_PRIVATE);
        int load = sharedPreferences.getInt("load", 0);
        if (load == 0) {
            CharSequence l = "Load";
            textLoadSave.setText(l);
        }
        else {
            CharSequence s = "Save";
            textLoadSave.setText(s);
        }
    }
    //LOAD SETTINGS KHI QUAY LẠI DOCTRUYEN
     @Override
    protected void onResume(){
        super.onResume();
        loadSettings();
    }
    //HÀM XỬ LÝ KHI ÁP DỤNG SETTING LÊN LISTVIEW
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

