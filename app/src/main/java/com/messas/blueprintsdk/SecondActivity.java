package com.messas.blueprintsdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    String selectcategory;
    String wight;
    String height;
    //connected or not
    FirebaseFirestore firebaseFirestore;
    int flag1  = 0;



    TextView macaddress, bluename111;
    RelativeLayout printcommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        macaddress=findViewById(R.id.macaddress);
        bluename111=findViewById(R.id.bluename111);
        printcommand=findViewById(R.id.printcommand);


        //get from intent
        try {
            Intent intent=getIntent();
            selectcategory = intent.getStringExtra("category");
            wight= intent.getStringExtra("wigth");
            height = intent.getStringExtra("height");
            if (TextUtils.isEmpty(selectcategory))
            {
                selectcategory = "ESC";
            }
            else{
                selectcategory = selectcategory;

            }
            //width
            if (TextUtils.isEmpty(wight))
            {
                wight = "284";
            }
            else{
                wight = wight;

            }
            //height
            if (TextUtils.isEmpty(height))
            {
                height = "384";
            }
            else{
                height = height;

            }

        }catch (Exception e)
        {
            Intent intent=getIntent();
            selectcategory = intent.getStringExtra("category");
            wight= intent.getStringExtra("wigth");
            height = intent.getStringExtra("height");
            if (TextUtils.isEmpty(selectcategory))
            {
                selectcategory = "ESC";
            }
            else{
                selectcategory = selectcategory;

            }
            //width
            if (TextUtils.isEmpty(wight))
            {
                wight = "284";
            }
            else{
                wight = wight;

            }
            //height
            if (TextUtils.isEmpty(height))
            {
                height = "384";
            }
            else{
                height = height;

            }
        }
        //get from intent
        //check connected or not and get mac address
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Connected_Device")
                .document("abc@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().exists())
                            {
                                try {

                                    //   Toast.makeText(TwoInchPrinterActivity.this, ""+task.getResult().getString("name"), Toast.LENGTH_SHORT).show();
                                    bluename111.setText(task.getResult().getString("name"));
                                    macaddress.setText(task.getResult().getString("mac"));
                                    connectedornot.setText("Connected");
                                    connectedornot.setTextColor(Color.parseColor("#006400"));
                                    Drawable icon = getResources().getDrawable(R.drawable.ic_connected);
                                    connectedornot.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

                                }catch (Exception e)
                                {
                                    Toast.makeText(SecondActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                connectedornot.setText("Not Connected");
                                connectedornot.setTextColor(Color.parseColor("#FF0000"));
                                Drawable icon = getResources().getDrawable(R.drawable.ic_not_connected);
                                connectedornot.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                            }
                        }
                    }
                });



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
                finishAffinity();
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



        ////speed density detect
/*

        firebaseFirestore.collection("DensityAndSpeed")
                .document("abc@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if (task.getResult().exists())
                            {
                                try {
                                    String speed = task.getResult().getString("speed");
                                    String density = task.getResult().getString("density");
                                    quantityProductPage_speed.setText(""+speed);
                                    seekBar.setProgress(Integer.parseInt(density));
                                    progressbarsechk.setText(""+density);
                                }catch (Exception e)
                                {

                                }

                            }
                            else{

                            }
                        }
                    }
                });

 */

        ///////
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
                geeet=macaddress.getText().toString();
            }
            else
            {
                geeet=macaddress.getText().toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();


        ////print  Section
        printcommand=findViewById(R.id.printcommand);
        //print Section
        printcommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BlueMac = macaddress.getText().toString();


                mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
                ///Toasty.info(getApplicationContext(),"Please active bluetooth"+mBluetoothAdapter.isEnabled(),Toasty.LENGTH_SHORT,true).show();
                if (!mBluetoothAdapter.isEnabled()) {
                    Toasty.info(getApplicationContext(), "Please active bluetooth", Toasty.LENGTH_SHORT, true).show();
                    android.app.AlertDialog.Builder mybuilder = new android.app.AlertDialog.Builder(SecondActivity.this);
                    mybuilder.setTitle("Confirmation")
                            .setMessage("Do you want to active bluetooth");
                    mybuilder.setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Right Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                mBluetoothAdapter.enable();
                                Toasty.info(getApplicationContext(), "Bluetooth is active now.", Toasty.LENGTH_SHORT, true).show();
                            } else {
                                mBluetoothAdapter.enable();
                                Toasty.info(getApplicationContext(), "Bluetooth is active now.", Toasty.LENGTH_SHORT, true).show();
                            }

                        }
                    }).create().show();

                    return;
                } else {
                    if (selectcategory.toString().toLowerCase().toString().equals("cpcl")){
                        Toasty.info(getApplicationContext(),"Bluetooth Device : "+bluename111.getText().toString()+"\n" +
                                "Mac Address : "+BlueMac,Toasty.LENGTH_SHORT,true).show();
                        printImage1(BlueMac);
                    }
                    else if (selectcategory.toString().toLowerCase().toString().equals("esc")){
                        Toasty.info(getApplicationContext(),"Bluetooth Device : "+bluename111.getText().toString()+"\n" +
                                "Mac Address : "+BlueMac,Toasty.LENGTH_SHORT,true).show();
                        printImage2(BlueMac);
                    }

                }
            }
        });

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
    ////bitmap and printing


    private  byte[]  BitmapToRGBbyteA(Bitmap bitmapOrg) {
        ArrayList<Byte> Gray_ArrayList;
        Gray_ArrayList =new ArrayList<Byte>();
        int height = 1080;
        if(bitmapOrg.getHeight()>height)
        {
            height=1080;
        }
        else
        {
            height=bitmapOrg.getHeight();
        }
        int width =30;
        int R = 0, B = 0, G = 0;
        //int pixles;
        int []pixels = new int[width * height];
        int x = 0, y = 0;
        Byte[] Gray_Send;
        //bitSet = new BitSet();
        try {

            bitmapOrg.getPixels(pixels, 0, width, 0, 0, width, height);
            int alpha = 0xFF << 24;
            //int []i_G=new int[7];
            int []i_G=new int[13];
            int Send_Gray=0x00;
            int StartInt=0;
            char  StartWords=' ';

            int k=0;
            int Send_i=0;
            int mathFlag=0;
            for(int i = 0; i < height; i++)
            {

                k=0;
                Send_i=0;
                for (int j = 0; j <width; j++)
                {
                    int grey = pixels[width * i + j];
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


//==================================
                    if(grey>128)
                    {
                        //bufImage[j]=0x00;
                        mathFlag=0;

                    }
                    else
                    {
                        //bufImage[j]=0x01;
                        mathFlag=1;
                    }
                    k++;
                    if(k==1)
                    {
                        Send_i=0;
                        Send_i=Send_i+128*mathFlag;//mathFlag|0x80
                    }
                    else if(k==2)
                    {
                        Send_i=Send_i+64*mathFlag;//mathFlag|0x40
                    }
                    else if(k==3)
                    {
                        Send_i=Send_i+32*mathFlag;//mathFlag|0x20
                    }
                    else if(k==4)
                    {
                        Send_i=Send_i+16*mathFlag;//mathFlag|0x10
                    }
                    else if(k==5)
                    {
                        Send_i=Send_i+8*mathFlag;//mathFlag|0x08
                    }
                    else if(k==6)
                    {
                        Send_i=Send_i+4*mathFlag;//mathFlag|0x04
                    }
                    else if(k==7)
                    {
                        Send_i=Send_i+2*mathFlag;//mathFlag|0x02
                    }
                    else if(k==8)
                    {
                        Send_i=Send_i+1*mathFlag;//mathFlag|0x01
                        Gray_ArrayList.add((byte)Send_i);

                        Send_i=0;
                        k=0;
                    }

                }
                int aBc=0;

            }

            byte[] sss=new byte[Gray_ArrayList.size()];
            Gray_Send=new Byte[Gray_ArrayList.size()];
            Gray_ArrayList.toArray(Gray_Send);
            for(int xx=0;xx<Gray_Send.length;xx++){
                sss[xx]=Gray_Send[xx];
            }
            return  sss;
        } catch (Exception e) {

        }
        return null;
    }
    int bitmapHeight = 1080;
    OutputStream os = null;
    private void printImage1(String bl) {
        //  final Bitmap bitmap = bitmapdataMe;
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.death);
        float scax=30f /bitmap.getWidth();
        float scaly= 30f / bitmap.getHeight();
        Log.e("dolon",""+bitmap);
        Log.e("zzz",""+bitmap.getWidth());
        Log.e("zzz",""+bitmap.getHeight());
        Matrix matrix=new Matrix();
        matrix.postScale(scax,scaly);
        Bitmap resize= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Log.e("Ariful9",""+scax);
        //final Bitmap bitmap = bitmapdataMe;

        final byte[] bitmapGetByte = BitmapToRGBbyteA(resize);//convertBitmapToRGBBytes (resize);
        Log.e("Ariful4",""+bitmapGetByte);
        String BlueMac = bl;
        Log.e("Ariful66",""+geeet);
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
        ///

        ////
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        m5ocket.connect();

                        os = m5ocket.getOutputStream();

                        if(resize.getHeight()>bitmapHeight)
                        {
                            bitmapHeight=1080;
                        }
                        else
                        {
                            bitmapHeight=resize.getHeight();
                        }
                        Log.e("Ariful1",""+resize.getWidth());
                        Log.e("Ariful2",""+resize.getHeight());
                        Log.e("Ariful3",""+bitmap);
                        Random random=new Random();
                        int sendingnumber=random.nextInt(10);
                        int mimisecond=sendingnumber*1000;

                        for (int i=1;i<=Integer.parseInt(quantityProductPage.getText().toString());i++){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    // write your code here
                                    countDownTimer =new CountDownTimer(2000,1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            double seconddd=millisUntilFinished/1000;
                                            printtimer.setText("Sending Data : "+seconddd+" S");



                                        }

                                        @Override
                                        public void onFinish() {
                                            try {
                                                String t_line1 = "! 0 200 200 "+bitmapHeight+" 1 \r\n";//bitmap.getHeight()
                                                String t_line2 = "pw "+30+"\r\n";
                                                String t_line3 = "DENSITY 12\r\n";
                                                String t_line4 = "SPEED 9\r\n";
                                                String t_line5 = "CG "+30/8+" "+bitmapHeight+" 0 0 ";
                                                String t_line6 ="PR 0\r\n";
                                                String t_line7= "FORM\r\n";
                                                String t_line8 = "PRINT\r\n";
                                                String t_line9 = "\r\n";
                                                os.write(t_line1.getBytes());
                                                os.write(t_line2.getBytes());
                                                os.write(t_line3.getBytes());
                                                os .write(t_line4.getBytes());
                                                os .write(t_line5.getBytes());

                                                os.write(bitmapGetByte);
                                                os .write(t_line9.getBytes());
                                                os .write(t_line6.getBytes());
                                                os.write(t_line7.getBytes());
                                                os.write(t_line8.getBytes());
                                                Log.e("Ariful5","PrintCommand");
                                            }catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("Ariful6",""+e.getMessage());
                                            }
                                            countDownTimer1=new CountDownTimer(1000,1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    long second=  (millisUntilFinished/1000);
                                                    int mysecond=Integer.parseInt(String.valueOf(second));



                                                }

                                                @Override
                                                public void onFinish() {

                                                    printtimer.setText("Print Out");
                                                    try {

                                                        os.flush();
                                                        os.flush();
                                                        m5ocket.close();
                                                        if (print_flag==0)
                                                        {
                                                            Store_Speed();
                                                            print_flag++;
                                                        }
                                                        else{
                                                            print_flag++;
                                                        }
                                                        Log.e("Ariful7","Go to print");

                                                    }catch (Exception e) {
                                                        e.printStackTrace();
                                                        Log.e("Ariful8",""+e.getMessage());
                                                    }

                                                    Toasty.success(getApplicationContext(),"Data Sending Complete",Toasty.LENGTH_SHORT,true).show();
                                                    return;
                                                }
                                            }.start();
                                            countDownTimer1.start();


                                        }
                                    };
                                    countDownTimer.start();
                                }
                            });
                        }

                    }
                    else {

                    }




                } catch (IOException e) {
                    // Toast.makeText(CPCLFresh.this, "Try Again. Bluetooth Connection Problem.", Toast.LENGTH_SHORT).show();
                    // printtimer.setText("Try Again. Bluetooth Connection Problem.");
                    Log.e("Error : ",""+e.getMessage());
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        thread.start();
    }
    //for esc

    private void printImage2(String bl) {
        //  final Bitmap bitmap = bitmapdataMe;

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dolphin);
        float scax=384f /bitmap.getWidth();
        float scaly=120f / bitmap.getHeight();
        Log.e("dolon",""+bitmap);
        Log.e("zzz",""+bitmap.getWidth());
        Log.e("zzz",""+bitmap.getHeight());
        Matrix matrix=new Matrix();
        matrix.postScale(scax,scaly);
        Bitmap resize= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        Log.e("Ariful9",""+scax);
        //final Bitmap bitmap = bitmapdataMe;

        final byte[] bitmapGetByte = BitmapToRGBbyteAA(resize);//convertBitmapToRGBBytes (resize);
        Log.e("Ariful4",""+bitmapGetByte);
        String BlueMac = bl;
        Log.e("Ariful66",""+bl);
        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                        m5ocket.connect();

                        os = m5ocket.getOutputStream();

                        if(resize.getHeight()>bitmapHeight)
                        {
                            bitmapHeight=1080;
                        }
                        else
                        {
                            bitmapHeight=resize.getHeight();
                        }
                        bitmapWidth=resize.getWidth();
                        Log.e("Ariful1",""+resize.getWidth());
                        Log.e("Ariful2",""+resize.getHeight());
                        Log.e("Ariful3",""+bitmap);
                        Random random=new Random();
                        int sendingnumber=random.nextInt(10);
                        int mimisecond=sendingnumber*1000;



                        for (int i=1;i<=Integer.parseInt(quantityProductPage.getText().toString());i++){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    // write your code here
                                    countDownTimer =new CountDownTimer(2000,1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            double seconddd=millisUntilFinished/1000;
                                            printtimer.setText("Sending Data : "+seconddd+" S");



                                        }

                                        @Override
                                        public void onFinish() {
                                            try {



                                                int StartInt=0;
                                                int PrintSpeedNow=3;
                                                int PrintDensityNow=15;
                                                int PrintPaperTypeNow=0;
                                                StartInt=0x1d;
                                                //Gray_Arraylist.add((byte)StartInt);
                                                os.write((byte)StartInt);
                                                StartInt=0x0e;
                                                os.write((byte)StartInt);
                                                StartInt=0x1c;
                                                os.write((byte)StartInt);
                                                StartInt=0x60;
                                                os.write((byte)StartInt);

                                                StartInt=0x4D;
                                                os.write((byte)StartInt);
                                                StartInt=0x53;
                                                os.write((byte)StartInt);
                                                if(PrintSpeedNow==6)
                                                {
                                                    StartInt=0x01;//PrintSpeedNow
                                                }
                                                else if(PrintSpeedNow==5)
                                                {
                                                    StartInt=0x0b;
                                                }
                                                else if(PrintSpeedNow==4)
                                                {
                                                    StartInt=0x15;
                                                }
                                                else if(PrintSpeedNow==3)
                                                {
                                                    StartInt=0x1f;
                                                }
                                                else if(PrintSpeedNow==2)
                                                {
                                                    StartInt=0x29;
                                                }
                                                else
                                                {
                                                    StartInt=0x33;
                                                }
                                                os.write((byte)StartInt);
                                                StartInt=0x1c;
                                                os.write((byte)StartInt);
                                                StartInt=0x60;
                                                os.write((byte)StartInt);
                                                StartInt=0x7E;
                                                os.write((byte)StartInt);
                                                StartInt=0x7E;
                                                os.write((byte)StartInt);
                                                StartInt=PrintDensityNow+0;//PrintDensityNow
                                                os.write((byte)StartInt);
                                                StartInt=0x1c;
                                                os.write((byte)StartInt);
                                                StartInt=0x60;
                                                os.write((byte)StartInt);
                                                StartInt=0x50;
                                                os.write((byte)StartInt);
                                                StartInt=0x50;
                                                os.write((byte)StartInt);
                                                StartInt=PrintPaperTypeNow+0;
                                                os.write((byte)StartInt);
                                                StartInt=0x1d;
                                                os.write((byte)StartInt);
                                                StartInt=0x76;
                                                os.write((byte)StartInt);
                                                StartInt=0x30;
                                                os.write((byte)StartInt);
                                                StartInt=0x00;
                                                os.write((byte)StartInt);
                                                int widthH=bitmapWidth/8/256;
                                                int widthL=bitmapWidth/8%256;
                                                int heightH=bitmapHeight/256;
                                                int heightL=bitmapHeight%256;
                                                StartInt=widthL+0;//PrintDensityNow
                                                os.write((byte)StartInt);
                                                StartInt=widthH+0;//PrintDensityNow
                                                os.write((byte)StartInt);
                                                StartInt=heightL+0;//PrintDensityNow
                                                os.write((byte)StartInt);
                                                StartInt=heightH+0;//PrintDensityNow
                                                os.write((byte)StartInt);
                                                os.write(bitmapGetByte);
                                                StartInt=0x1c;
                                                os.write((byte)StartInt);
                                                StartInt=0x5e;
                                                os.write((byte)StartInt);
                                                Log.e("Ariful5","PrintCommand");
                                            }catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("Ariful6",""+e.getMessage());
                                            }
                                            countDownTimer1=new CountDownTimer(1000,1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    long second=  (millisUntilFinished/1000);
                                                    int mysecond=Integer.parseInt(String.valueOf(second));



                                                }

                                                @Override
                                                public void onFinish() {

                                                    printtimer.setText("Print Out");
                                                    try {

                                                        os.flush();
                                                        os.flush();
                                                        m5ocket.close();
                                                        if (print_flag==0)
                                                        {
                                                            Store_Speed();
                                                            print_flag++;
                                                        }
                                                        else{
                                                            Store_Speed();
                                                        }
                                                        Log.e("Ariful7","Go to print");

                                                    }catch (Exception e) {
                                                        e.printStackTrace();
                                                        Log.e("Ariful8",""+e.getMessage());
                                                    }

                                                    Toasty.success(getApplicationContext(),"Data Sending Complete",Toasty.LENGTH_SHORT,true).show();
                                                    return;
                                                }
                                            }.start();
                                            countDownTimer1.start();


                                        }
                                    };
                                    countDownTimer.start();
                                }
                            });
                        }

                    }
                    else {

                    }




                } catch (IOException e) {
                    // Toast.makeText(CPCLFresh.this, "Try Again. Bluetooth Connection Problem.", Toast.LENGTH_SHORT).show();
                    // printtimer.setText("Try Again. Bluetooth Connection Problem.");
                    Log.e("Error : ",""+e.getMessage());
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, e.getMessage(), Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        thread.start();

    }
    private  byte[]  BitmapToRGBbyteAA(Bitmap bitmapOrg) {
        ArrayList<Byte> Gray_ArrayList;
        Gray_ArrayList =new ArrayList<Byte>();
        int height =1080;
        if(bitmapOrg.getHeight()>height)
        {
            height=1080;
        }
        else
        {
            height=bitmapOrg.getHeight();
        }
        int width =bitmapOrg.getWidth();
        int R = 0, B = 0, G = 0;
        //int pixles;
        int []pixels = new int[width * height];
        int x = 0, y = 0;
        Byte[] Gray_Send;
        //bitSet = new BitSet();
        try {

            bitmapOrg.getPixels(pixels, 0, width, 0, 0, width, height);
            int alpha = 0xFF << 24;
            //int []i_G=new int[7];
            int []i_G=new int[13];
            int Send_Gray=0x00;
            int StartInt=0;
            char  StartWords=' ';

            int k=0;
            int Send_i=0;
            int mathFlag=0;
            for(int i = 0; i < height; i++)
            {

                k=0;
                Send_i=0;
                for (int j = 0; j <width; j++)
                {
                    int grey = pixels[width * i + j];
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


//==================================
                    if(grey>128)
                    {
                        //bufImage[j]=0x00;
                        mathFlag=0;

                    }
                    else
                    {
                        //bufImage[j]=0x01;
                        mathFlag=1;
                    }
                    k++;
                    if(k==1)
                    {
                        Send_i=0;
                        Send_i=Send_i+128*mathFlag;//mathFlag|0x80
                    }
                    else if(k==2)
                    {
                        Send_i=Send_i+64*mathFlag;//mathFlag|0x40
                    }
                    else if(k==3)
                    {
                        Send_i=Send_i+32*mathFlag;//mathFlag|0x20
                    }
                    else if(k==4)
                    {
                        Send_i=Send_i+16*mathFlag;//mathFlag|0x10
                    }
                    else if(k==5)
                    {
                        Send_i=Send_i+8*mathFlag;//mathFlag|0x08
                    }
                    else if(k==6)
                    {
                        Send_i=Send_i+4*mathFlag;//mathFlag|0x04
                    }
                    else if(k==7)
                    {
                        Send_i=Send_i+2*mathFlag;//mathFlag|0x02
                    }
                    else if(k==8)
                    {
                        Send_i=Send_i+1*mathFlag;//mathFlag|0x01
                        Gray_ArrayList.add((byte)Send_i);

                        Send_i=0;
                        k=0;
                    }

                }
                int aBc=0;

            }


            byte[] sss=new byte[Gray_ArrayList.size()];
            Gray_Send=new Byte[Gray_ArrayList.size()];
            Gray_ArrayList.toArray(Gray_Send);
            for(int xx=0;xx<Gray_Send.length;xx++){
                sss[xx]=Gray_Send[xx];
            }
            return  sss;
        } catch (Exception e) {

        }
        return null;
    }
    int print_flag = 0;

    int bitmapWidth=384;
    public  void Store_Speed()
    {
        String density = progressbarsechk.getText().toString();
        String speed = quantityProductPage_speed.getText().toString();
        String email = "abc@gmail.com";
        DensityModel densityModel=new DensityModel(speed,density,email);

        firebaseFirestore.collection("DensityAndSpeed")
                .document("abc@gmail.com")
                .set(densityModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}