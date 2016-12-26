package com.hvstudy.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv;
    int temp;
    public static String username = String.valueOf(R.string.user_not_login_title);
    EditText search;
    public static TextView usertoshow;
    public static int isLogined = 0;
    JSONArray mang = null;
    String danhgia;
    final ArrayList<Tuadetruyen> mangtruyen = new ArrayList<Tuadetruyen>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView)findViewById(R.id.lvMain1);

        SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
        isLogined = share.getInt("Logined", 0);
        username = share.getString("Name",getString(R.string.user_not_login_title));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Thêm navigator từ activity khác để settext được cho tên usertoshow
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView= navigationView.getHeaderView(0);
        usertoshow = (TextView)headerView.findViewById(R.id.txtUsertoshow);
        usertoshow.setText(username);
        search = (EditText)findViewById(R.id.edtSearch);

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               new DocJson().execute("http://hvtekshop.com/json.php");
           }
       });

        //// USER NHẤN VÀO ITEM ĐỂ XEM CHI TIẾT TRUYỆN
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

        //// USER NHẤN GIỮ VÀO ITEM ĐỂ ĐÁNH GIÁ
        final String myarr[] = {"1","2","3","4","5"};
        final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle(getString(R.string.rate_question));
        ad.setPositiveButton(getString(R.string.rate_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this,mangtruyen.get(temp).id + "---" + danhgia, Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new callURL().execute("http://hvtekshop.com/danhgia.php?idtr="+mangtruyen.get(temp).id+"&diem="+danhgia);
                    }
                });
                arg0.dismiss();
            }
        });
        ad.setSingleChoiceItems(myarr,0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                danhgia = myarr[which];
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int x, long l) {
                temp = x;
                if(isLogined == 0)
                {
                    Toast.makeText(MainActivity.this,getString(R.string.user_not_login_toast),Toast.LENGTH_SHORT).show();
                }
                else {
                    ad.show();
                }
                return false;
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

// KHU VỰC MENU SETTING
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent preferencesIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(preferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override

//KHU VỰC THANH SLIDE MENU BÊN TRÁI - QUẢN LÝ CÁC ITEM KHI NHẤN VÀO
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navLastest) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/lastest.php");
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new DocJson().execute("http://hvtekshop.com/popular.php");
                }
            });
        }
        if(id == R.id.nav_register)
        {
            if(MainActivity.isLogined == 1)
                Toast.makeText(MainActivity.this,getString(R.string.loggedin),Toast.LENGTH_SHORT).show();
            else {
                Intent toDangky = new Intent(MainActivity.this, Dangky.class);
                startActivityForResult(toDangky, 1);
            }
        }
        else if (id == R.id.nav_sign_in)
        {
            if(MainActivity.isLogined == 1)
                Toast.makeText(MainActivity.this,getString(R.string.loggedin),Toast.LENGTH_SHORT).show();
            else {
                Intent toDangnhap = new Intent(MainActivity.this, DangNhap.class);
                startActivityForResult(toDangnhap, 1);
            }
        } else if (id == R.id.nav_favorite)
        {
            if(MainActivity.isLogined == 0)
                Toast.makeText(MainActivity.this,getString(R.string.user_not_login_toast),Toast.LENGTH_SHORT).show();
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new DocJson().execute("http://hvtekshop.com/showfavorite.php?usr="+MainActivity.username);
                    }
                });

            }
        } else if (id == R.id.nav_sign_out)
        {
            if(MainActivity.isLogined == 0)
                Toast.makeText(MainActivity.this,getString(R.string.user_not_login_toast),Toast.LENGTH_SHORT).show();
            else {
                isLogined = 0;
                username = getString(R.string.user_not_login_title);
                usertoshow.setText(username);
                SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString("Name",username);
                editor.putInt("Logined", 0);
                editor.commit();
                Toast.makeText(MainActivity.this, getString(R.string.signedout), Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // XỬ LÝ DỮ LIỆU KHI NÓ ĐƯỢC TRẢ VỀ TỪ ACTIVITY KHÁC
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2 && requestCode == 1)
        {
            SharedPreferences share = getSharedPreferences("MyShare", MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit();
            usertoshow.setText(getString(R.string.user_hello)+data.getStringExtra("user"));
            isLogined=1;
            editor.putString("Name",data.getStringExtra("user"));
            editor.putInt("Logined", 1);
            editor.commit();
        }
    }

   // ĐỌC NỘI DUNG JSON TỪ WEB VÀ THỰC HIỆN CÁC XỬ LÝ
    class DocJson extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mangtruyen.clear();
            mang = null;

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


        //CODE CỦA TÍNH NĂNG TÌM KIẾM
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Tuadetruyen> search = new ArrayList<Tuadetruyen>();
                String s = charSequence.toString().toUpperCase();
                for(int x = 0;x<mangtruyen.size();x++)
                {
                    if(mangtruyen.get(x).tentruyen.toUpperCase().contains(s))
                        search.add(mangtruyen.get(x));
                }
                AdapterTruyen adapter1 = new AdapterTruyen(MainActivity.this,R.layout.adapterlayout,search);
                lv.setAdapter(adapter1);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    }


    // HÀM KẾT NỐI INTERNET QUA URL VÀ NHẬN DỮ LIỆU VỀ
    public static String docNoiDung_Tu_URL(String theUrl)
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

    class callURL extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}

