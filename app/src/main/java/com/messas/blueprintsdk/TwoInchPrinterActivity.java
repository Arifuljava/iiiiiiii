package com.messas.blueprintsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import es.dmoral.toasty.Toasty;

public class TwoInchPrinterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView quantityProductPage,quantityProductPage_speed;
    SeekBar seekBar;
    TextView progressbarsechk;
    TextView connectedornot;
    String geeet;

    /////bitmap data
    Uri imageuri;
    int flag = 0;
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    ImageView imageposit;

    Button printimageA;
    Bitmap bitmapdataMe;
    TextView printtimer;
    Spinner papertype;
    String valueSpinner;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);


        papertype=findViewById(R.id.papertype);
        papertype.setOnItemSelectedListener(this);
        quantityProductPage_speed=findViewById(R.id.quantityProductPage_speed);

        String[] textSizes = getResources().getStringArray(R.array.papersize);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.selectitem, textSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        papertype.setAdapter(adapter);

        printtimer=findViewById(R.id.printtimer);
        quantityProductPage=findViewById(R.id.quantityProductPage);
        progressbarsechk=findViewById(R.id.progressbarsechk);
        connectedornot=findViewById(R.id.connectedornot);
        seekBar=findViewById(R.id.seekBar);
        ImageView closedialouge=findViewById(R.id.closedialouge);
        closedialouge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressbarsechk=findViewById(R.id.progressbarsechk);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                progressbarsechk.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //   Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });




        RelativeLayout relagoo=findViewById(R.id.relagoo);
        relagoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         startActivity(new Intent(getApplicationContext(),Findlocation.class));
            }
        });

        //getdata
        try {
            geeet=getIntent().getStringExtra("geeet");
            if (TextUtils.isEmpty(geeet)|| geeet.equals(null))
            {
                geeet="FB:7F:9B:F2:20:B7";
            }
            else
            {
                geeet=geeet;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(geeet);

    }
    public void decrement(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==1) {
            Toasty.info(getApplicationContext(),"It is the lowest value.Print Copy value is not decrement now.", Toast.LENGTH_SHORT,true).show();
          //  Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value-1;
            quantityProductPage.setText(""+value);
        }
    }

    public void increment(View view) {
        int value = Integer.parseInt(quantityProductPage.getText().toString());
        if (value==99) {
            Toasty.warning(getApplicationContext(),"It is the highest value. Print Copy value is not increment now.", Toast.LENGTH_SHORT,true).show();
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value+1;
            quantityProductPage.setText(""+value);
        }
    }


    //print section
    Uri bitmapUri;
    Bitmap mainimageBitmap;

    int PICK=12;
    boolean request=false;
    CountDownTimer countDownTimer,countDownTimer1;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void decrement_speed(View view) {
        int value = Integer.parseInt(quantityProductPage_speed.getText().toString());
        if (value==1) {
            Toasty.info(getApplicationContext(),"It is the lowest value.Print speed value is not decrement now.", Toast.LENGTH_SHORT,true).show();
            //Toast.makeText(this, "It is the lowest value.Print speed value is not decrement now.", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value-1;
            quantityProductPage_speed.setText(""+value);
        }
    }

    public void increment_speed(View view) {
        int value = Integer.parseInt(quantityProductPage_speed.getText().toString());
        if (value==6) {
            Toasty.warning(getApplicationContext(),"It is the highest value. Print Speed value is not increment now.", Toast.LENGTH_SHORT,true).show();
         //   Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        else{
            value=value+1;
            quantityProductPage_speed.setText(""+value);
        }
    }

    public void dialougeccc(View view) {
        finishAffinity();
    }
}