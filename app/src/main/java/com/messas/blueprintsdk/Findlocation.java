package com.messas.blueprintsdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Findlocation extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDeviceAdapter deviceAdapter;
    private ListView listView;
    private BroadcastReceiver discoveryReceiver;
    BluetoothDevice device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findlocation);





        // Request necessary permissions
        requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        // Initialize Bluetooth adapter and device adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter = new BluetoothDeviceAdapter(this, new ArrayList<>());
        listView = findViewById(R.id.list_view);
        listView.setAdapter(deviceAdapter);
    // listView.setVisibility(View.INVISIBLE);

        // Register BroadcastReceiver for discovered devices
        discoveryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                     device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    deviceAdapter.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);

        // Check if Bluetooth is supported and enabled
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled, prompt the user to enable it
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            // Bluetooth is enabled, start device discovery
            bluetoothAdapter.startDiscovery();
        }


        //// checking

        Dialog mDialouge=new Dialog(Findlocation.this);
        mDialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialouge.setContentView(R.layout.initilizeallinformation);
        mDialouge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  if(listView.le)

                int itemCount = deviceAdapter.getCount();
               if(itemCount==0)
               {
                   mDialouge.dismiss();
                   Toasty.error(getApplicationContext(),itemCount+" devices found on your location.",Toasty.LENGTH_SHORT,true).show();
               }
               else{
                   mDialouge.dismiss();
                   listView.setVisibility(View.VISIBLE);
                   Toasty.success(getApplicationContext(),itemCount+" devices found on your location.",Toasty.LENGTH_SHORT,true).show();
               }

            }
        },5000);



        mDialouge.create();;
        mDialouge.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled, start device discovery
                bluetoothAdapter.startDiscovery();
            } else {
                Toast.makeText(this, "Bluetooth is required to discover devices", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister BroadcastReceiver
        unregisterReceiver(discoveryReceiver);
    }

    public void closedialouge(View view) {
        startActivity(new Intent(getApplicationContext(),TwoInchPrinterActivity.class));
    }

    private static class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
        private LayoutInflater inflater;

        public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> devices) {
            super(context, 0, devices);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.show2, parent, false);
            }

            BluetoothDevice device = getItem(position);
            TextView deviceNameTextView = view.findViewById(R.id.listedd);


            deviceNameTextView.setText(device.getName()+"\n"+device.getAddress());

            RelativeLayout carditem=view.findViewById(R.id.carditem);




            carditem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), device.getName()+"\n"+device.getAddress(), Toast.LENGTH_SHORT).show();
                    String BlueMac = "FB:7F:9B:F2:20:B7";


                }
            });
            return view;
        }
    }
}