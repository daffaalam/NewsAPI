package com.daffaalam.newsapi.tools;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("Registered")
public class Functions extends AppCompatActivity {

    public static final String SOURCE = "source";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String URL = "url";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String CONTENT = "content";

    private ProgressDialog progressDialog;

    public void myLog(String logs) {
        Log.v("myLOG", logs);
    }

    protected void myToast(String pesan) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }

    public void myIntent(Class tujuan) {
        startActivity(new Intent(this, tujuan));
    }

    /*
    public void myProgressDialog(boolean tampil) {
        if (tampil) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("loading...");
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }
    */
}
