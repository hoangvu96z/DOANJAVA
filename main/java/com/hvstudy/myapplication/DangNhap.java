package com.hvstudy.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DangNhap extends AppCompatActivity {
    EditText user, pass;
    Button go;
    String u,p;
    String ketqua;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        user = (EditText) findViewById(R.id.edtSign_User);
        pass = (EditText) findViewById(R.id.edtSign_Pass);
        go = (Button) findViewById(R.id.btnSignIn);
        if(MainActivity.isLogined == 1)
        {
            Intent intent = new Intent(DangNhap.this,MainActivity.class);
            startActivity(intent);
        }
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        u = user.getText().toString();
                        p = pass.getText().toString();
                        new Kiemtranguoidung().execute("http://hvtekshop.com/signin.php?usr="+u+"&pas="+p);
                    }
                });
            }
        });
    }

    class Kiemtranguoidung extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            return MainActivity.docNoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            JSONArray mang = null;
            try {
                mang = new JSONArray(s);
                JSONObject truyen = mang.getJSONObject(0);
                ketqua = truyen.getString("ketqua");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (u.equals("") || p.equals("")) {
                Toast.makeText(DangNhap.this, getString(R.string.signup_fill_in), Toast.LENGTH_SHORT).show();
            } else {
                if (ketqua.equals("true")) {
                    Toast.makeText(DangNhap.this, getString(R.string.regester_successful), Toast.LENGTH_SHORT).show();
                    ketqua = "false";
                    Intent resultMain = new Intent();
                    resultMain.putExtra("user", u);
                    setResult(2, resultMain);
                    MainActivity.username = u;
                    finish();
                } else {
                    Toast.makeText(DangNhap.this, getString(R.string.regester_successful), Toast.LENGTH_SHORT).show();
                    pass.setText("");
                }
            }
        }
    }

}
