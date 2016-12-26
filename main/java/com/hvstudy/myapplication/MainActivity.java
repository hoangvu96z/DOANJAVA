package com.hvstudy.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
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

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lv;
    TextView usertoshow;
    int isLogined = 0;
    final String[] danhgia = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView)findViewById(R.id.lvMain1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView= navigationView.getHeaderView(0);
        usertoshow = (TextView)headerView.findViewById(R.id.txtUsertoshow);
        usertoshow.setText("CHƯA ĐĂNG NHẬP");


       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               new DocJson().execute("http://hvtekshop.com/json.php");
           }
       });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navLastest) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/json.php");
                }
            });
        } else if (id == R.id.navTruyenMa) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/searchtheloai.php?id_theloai=3");
                }
            });
        } else if (id == R.id.navKiemHiep) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/searchtheloai.php?id_theloai=1");
                }
            });
        } else if (id == R.id.navNgonTinh) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/searchtheloai.php?id_theloai=2");
                }
            });
        } else if (id == R.id.navTruyenTeen) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/searchtheloai.php?id_theloai=5");
                }
            });
        } else if (id == R.id.navTruyenTrinhTham) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/searchtheloai.php?id_theloai=4");
                }
            });
        } else if (id == R.id.navPopular)
        {

        }
        if(id == R.id.nav_register)
        {
            Intent toDangky = new Intent(MainActivity.this,Dangky.class);
//            Bundle dulieu = new Bundle();
//            dulieu.putInt("isLogined",isLogined);
//            dulieu.putString("username",user);
//            toDangky.putExtra("goitin",dulieu);
            startActivityForResult(toDangky,1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        if(requestCode==2)
        {
            usertoshow.setText(data.getStringExtra("username"));
            Toast.makeText(MainActivity.this,"co DU LIEU",Toast.LENGTH_LONG);
        }
    }

    // đọc nội dung JSON và hiện lên danh sách
    class DocJson extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           //Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();

            final ArrayList<Tuadetruyen> mangtruyen = new ArrayList<Tuadetruyen>();
        JSONArray mang = null;
        try {
            mang = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0; i<mang.length();i++)
        {
            try {
                JSONObject truyen = mang.getJSONObject(i);
                mangtruyen.add(new Tuadetruyen(
                        parseInt(truyen.getString("id")),
                        truyen.getString("tentruyen"),
                        Float.parseFloat(truyen.getString("danhgia")),
                        truyen.getString("URLhinh")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final AdapterTruyen adapter = new AdapterTruyen(MainActivity.this,R.layout.adapterlayout,mangtruyen);
        lv.setAdapter(adapter);


            //// USER NHẤN VÀO ITEM ĐỂ XEM CHI TIẾT Ở ACTIVITY KHÁC
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent toNoidung = new Intent(MainActivity.this,Doctruyen.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID",mangtruyen.get(i).id);
                    toNoidung.putExtra("data",bundle);
                    startActivity(toNoidung);
                }
            });


            //// USER NHẤN GIỮ ĐỂ ĐÁNH GIÁ
            final String myarr[] = {"1","2","3","4","5"};
            final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("Bạn đánh giá truyện này bao nhiêu điểm ?");
            ad.setItems(myarr,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    danhgia[0] = myarr[arg1];
                    //Toast.makeText(getApplicationContext(),"Bạn đã đánh giá: " + danhgia[0]+" điểm. Cảm ơn đánh giá của bạn !", Toast.LENGTH_SHORT).show();

                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                    ad.show();
                    docNoiDung_Tu_URL("http://hvtekshop.com/danhgia.php?idtr="+mangtruyen.get(i).id+"&diem="+danhgia[0]);
                    Toast.makeText(MainActivity.this,"http://hvtekshop.com/danhgia.php?idtr="+mangtruyen.get(i).id+"&diem="+danhgia[0],Toast.LENGTH_SHORT);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });
    }
    }

    //Đọc nội dung từ URL
    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
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
}
