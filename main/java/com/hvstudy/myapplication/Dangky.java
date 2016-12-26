package com.hvstudy.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Dangky extends AppCompatActivity {
    EditText usr,pas,repas;
    Button sgup;
    String ketqua="false";
    String ten,mk,nlmk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usr = (EditText) findViewById(R.id.edtUserName);
        pas = (EditText) findViewById(R.id.edtPass);
        repas = (EditText) findViewById(R.id.edtRePass);
        sgup = (Button) findViewById(R.id.btnSignUp);

        sgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ten = usr.getText().toString();
                mk = pas.getText().toString();
                nlmk = repas.getText().toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Kiemtranguoidung().execute("http://hvtekshop.com/signup.php?usr="+ten+"&pas="+mk);
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
            // Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            JSONArray mang = null;
            try {
                mang = new JSONArray(s);
                JSONObject truyen = mang.getJSONObject(0);
                ketqua = truyen.getString("ketqua");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mk.equals("") || ten.equals("") || nlmk.equals("")) {
                Toast.makeText(Dangky.this, getString(R.string.signup_fill_in), Toast.LENGTH_SHORT).show();
            } else if (mk.equals(nlmk)) {
                if (ketqua.equals("true")) {
                    Toast.makeText(Dangky.this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                    ketqua = "false";
                    Intent resultMain = new Intent();
                    MainActivity.username= ten;
                    MainActivity.isLogined = 1;
                    resultMain.putExtra("user", ten);
                    setResult(2, resultMain);
                    finish();
                } else {
                    Toast.makeText(Dangky.this, getString(R.string.signup_unsuccessful), Toast.LENGTH_SHORT).show();
                    usr.setText("");
                    pas.setText("");
                    repas.setText("");
                }

            } else {
                Toast.makeText(Dangky.this, getString(R.string.signup_error), Toast.LENGTH_SHORT).show();
                pas.setText("");
                repas.setText("");
            }

        }
    }



}
