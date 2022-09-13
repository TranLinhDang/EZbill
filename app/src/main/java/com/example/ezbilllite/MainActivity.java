package com.example.ezbilllite;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothGatt mBluetoothGatt;
    private String mBluetoothDeviceAddress;

    private boolean mScanning;
    private Handler mHandler;

    private long scanPeriod = 5000;
    private int signalStrength = -75;


    private HashMap<String, BTLE_Device> mBTDevicesHashMap;
    public ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;
    private ListView listView;

    private Button btn_Scan;

    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

//    public final static String ACTION_GATT_CONNECTED =
//            "com.example.ezbilllite.ACTION_GATT_CONNECTED";
//    public final static String ACTION_GATT_DISCONNECTED =
//            "com.example.ezbilllite.ACTION_GATT_DISCONNECTED";
//    public final static String ACTION_GATT_SERVICES_DISCOVERED =
//            "com.example.ezbilllite.ACTION_GATT_SERVICES_DISCOVERED";
//    public final static String ACTION_DATA_AVAILABLE =
//            "com.example.ezbilllite.ACTION_DATA_AVAILABLE";
//    public final static String EXTRA_DATA =
//            "com.example.ezbilllite.EXTRA_DATA";
//    public final static String EXTRA_UUID = "com.example.ezbilllite.EXTRA_UUID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BTLE_Devices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);

        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        findViewById(R.id.btn_scan).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "PLease turn on Bluetooth", Toast.LENGTH_SHORT).show();
            }
//        } else if (requestCode == BTLE_SERVICES) {
//            // Do something
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

//        Utils.toast(context, "List Item clicked");

        // do something with the text views and start the next activity.

        stopScan();

        String name = mBTDevicesArrayList.get(position).getName();
        String address = mBTDevicesArrayList.get(position).getAddress();



        Toast.makeText(this, name + " clicked", Toast.LENGTH_SHORT).show();
//        connect(address);
//        Intent intent = new Intent(this, ActivityServices.class);

        final Intent intent = new Intent(this, DeviceControlActivity.class);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, name);
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, address);

//        Intent intent = new Intent(this, Activity_BTLE_Services.class);
//        intent.putExtra(Activity_BTLE_Services.EXTRA_NAME, name);
//        intent.putExtra(Activity_BTLE_Services.EXTRA_ADDRESS, address);
        startActivity(intent);
//        startActivityForResult(intent, BTLE_SERVICES);
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        final BluetoothDevice device = mBluetoothAdapter.getD .getDevice(position);
//        if (device == null) return;
//        final Intent intent = new Intent(this, DeviceControlActivity.class);
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
//        if (mScanning) {
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            mScanning = false;
//        }
//        startActivity(intent);
//    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_scan:
                if (!isScanning()) {
                    startScan();
                }
                else {
                    stopScan();
                }

                break;
            default:
                break;
        }

    }

    public void addDevice(BluetoothDevice device, int rssi) {

        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRSSI(rssi);

            mBTDevicesHashMap.put(address, btleDevice);
            mBTDevicesArrayList.add(btleDevice);
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan(){
        btn_Scan.setText("Scanning...");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        start();
    }

    public void stopScan() {
        btn_Scan.setText("Scan");

        stop();
    }

    ///////////////////////////////////////////////

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {
        scanLeDevice(true);
    }

    public void stop() {
        scanLeDevice(false);
    }

    // If you want to scan for only specific types of peripherals,
    // you can instead call startLeScan(UUID[], BluetoothAdapter.LeScanCallback),
    // providing an array of UUID objects that specify the GATT services your app supports.
    private void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);

                    stopScan();
                }
            }, scanPeriod);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
//            mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
        }
        else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {

                    final int new_rssi = rssi;
                    if (rssi > signalStrength) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                addDevice(device, new_rssi);
                            }
                        });
                    }
                }
            };

    //////////////////

//    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            String intentAction;
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                intentAction = ACTION_GATT_CONNECTED;
//
//                mConnectionState = STATE_CONNECTED;
//
//                broadcastUpdate(intentAction);
//
//                Log.i(TAG, "Connected to GATT server.");
//                // Attempts to discover services after successful connection.
//                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices() + "AAAA");
//
//                System.out.println("CCCCCCCCCC");
//            }
//            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                intentAction = ACTION_GATT_DISCONNECTED;
//
//                mConnectionState = STATE_DISCONNECTED;
//
//                Log.i(TAG, "Disconnected from GATT server.");
//
//                broadcastUpdate(intentAction);
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
//            }
//            else {
//                Log.w(TAG, "onServicesDiscovered received: " + status + "BBBB");
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt,
//                                         BluetoothGattCharacteristic characteristic,
//                                         int status) {
//
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt,
//                                            BluetoothGattCharacteristic characteristic) {
//
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//        }
//
//    };
//
//    private void broadcastUpdate(final String action) {
//        final Intent intent = new Intent(action);
//        sendBroadcast(intent);
//    }
//
//    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
//
//        final Intent intent = new Intent(action);
//
//        intent.putExtra(EXTRA_UUID, characteristic.getUuid().toString());
//
//        // For all other profiles, writes the data formatted in HEX.
//        final byte[] data = characteristic.getValue();
//
//        if (data != null && data.length > 0) {
//
//            intent.putExtra(EXTRA_DATA, new String(data) + "\n");
//        }
//        else {
//            intent.putExtra(EXTRA_DATA, "0");
//        }
//
//        sendBroadcast(intent);
//    }
//
//    public boolean connect(final String address) {
//
//        if (mBluetoothAdapter == null || address == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
//            return false;
//        }
//
//        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            }
//            else {
//                return false;
//            }
//        }
//
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//
//        if (device == null) {
//            Log.w(TAG, "Device not found.  Unable to connect.");
//            return false;
//        }
//
//        // We want to directly connect to the device, so we are setting the autoConnect
//        // parameter to false.
//        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
//        Log.d(TAG, "Trying to create a new connection.");
//        mBluetoothDeviceAddress = address;
//        mConnectionState = STATE_CONNECTING;
//
//        return true;
//    }
//
//    public void disconnect() {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//
//        mBluetoothGatt.disconnect();
//    }
//
//    public void close() {
//
//        if (mBluetoothGatt == null) {
//            return;
//        }
//
//        mBluetoothGatt.close();
//        mBluetoothGatt = null;
//    }
//
//    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//
//        mBluetoothGatt.readCharacteristic(characteristic);
//    }
//
//    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//
//        mBluetoothGatt.writeCharacteristic(characteristic);
//    }
//
//    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
//
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//
//        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
//                UUID.fromString(getString(R.string.CLIENT_CHARACTERISTIC_CONFIG)));
//
//        if (enabled) {
//            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        }
//        else {
//            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//        }
//
//        mBluetoothGatt.writeDescriptor(descriptor);
//    }

//    public List<BluetoothGattService> getSupportedGattServices() {
//
//        if (mBluetoothGatt == null) {
//            return null;
//        }
//
//        return mBluetoothGatt.getServices();
//    }


}
