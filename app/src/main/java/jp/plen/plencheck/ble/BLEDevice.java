package jp.plen.plencheck.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by yuki on 3/30/15.
 */
//45
public class BLEDevice {
    private Context context;
    private BLECallbacks bleCallback;
    private final static String TAG = "BLEDevice";
    private BluetoothGatt gatt;
    private ArrayList<String> deviceList = new ArrayList<String>();
    private boolean isConnected;
    public interface BLECallbacks{
        public void onConnected(String deviceName);
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" + gatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = gatt.getServices();
                //TODO: Check service UUID
                for(BluetoothGattService service : services){
                    Log.i(TAG, "Support service UUID : " + service.getUuid().toString());
                }
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }
    };

    public BLEDevice(Context context){
        this.context = context;

        //Get a Bluetooth Adapter
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothManager == null){
            Log.e(TAG, "Unable to initialize BluetoothManager.");
        }
        final BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        //TODO: Add startActivityForResult to start "Enable Bluetooth" activity.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Unable to initialize BluetoothAdapter.");
        }

        //Search BLE Device
        final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                Log.d(TAG, "found: " + device.getName() + " addr:" + device.getAddress() + " rssi: " + rssi);

                // ここでデバイス名やアドレスなどを見て接続処理を行う
                isConnected = connect(device);
                if (isConnected && bleCallback!=null){
                    bleCallback.onConnected(device.getName());
                    bluetoothAdapter.stopLeScan(this);
                }

            }
        };

        final long SCAN_PERIOD = 5000;
        new Thread() {
            @Override
            public void run() {
                bluetoothAdapter.startLeScan(callback);
                try {
                    Thread.sleep(SCAN_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bluetoothAdapter.stopLeScan(callback);
            }
        }.start();
    }

    public void setBLECallbacks(BLECallbacks bleCallback){
        this.bleCallback = bleCallback;
    }

    public boolean connect(BluetoothDevice bluetoothDevice){
        if(bluetoothDevice == null){
            Log.e(TAG, "Device not found. Unable to connect a device");
            return false;
        }
        //TODO: Previously connected device. Try to reconnect.

        // Setting autoConnect parameter false to connect bluetooth device directly.
        gatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
        return true;
    }

    public void close(){
        if (gatt == null) {
            return;
        }
        gatt.close();
        gatt = null;
    }

    public boolean write(String str){
        if (isConnected) {
            BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString(GATTAttributes.BLE_SHIELD_SERVICE));
            if (bluetoothGattService == null) {
                Log.e(TAG, "Can't get a BluetoothGattService");
                return false;
            }
            BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(UUID.fromString(GATTAttributes.BLE_SHIELD_TX));
            if (characteristic == null) {
                Log.e(TAG, "Can't get a BluetoothGattCharacteristic");
                return false;
            }
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            characteristic.setValue(str);
            gatt.writeCharacteristic(characteristic);

            return true;
        }
        else {
            return false;
        }
    }
}
