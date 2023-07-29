package com.messas.blueprintsdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.system.Os;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
Os operating_Sytem;
TextView mity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mity=findViewById(R.id.mity);
    startActivity(new Intent(getApplicationContext(),TwoInchPrinterActivity.class));
       /*
        PackageManager pm = MainActivity.this.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_PC))
        {
            mity.setText("Chrome OS");
            Toast.makeText(this, "Chrome OS", Toast.LENGTH_SHORT).show();
        }
        else{
            mity.setText("Android");
            Toast.makeText(this, "Android", Toast.LENGTH_SHORT).show();
        }
        */
        MultiDex.install(MainActivity.this);

    }
}