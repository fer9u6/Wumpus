package isl.wumpus;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActiviy";
    BluetoothAdapter mBluetoothAdapter;
    Button btnDiscover;

    Button btnStartConnection;
    Button btnSend;

    TextView incomingMessages;
    StringBuilder messages;

    BluetoothConnection mBluetoothConnection;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    EditText chatText;

    public ArrayList<BluetoothDevice> mBLDevice = new ArrayList<>();
    public DeviceAdapter mDeviceAdapter;
    ListView listDeviceView;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        mBluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onRecieve State OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "aBroadcastReceiver1:State Truning Off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "aBroadcastReceiver1:StateON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "aBroadcastReceiver1:State turning on");
                        break;
                }
            }
        }
    };

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,
                        BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "Discoverable Enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "Adapter is Connectable");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "No Scan Mode");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "Device is connecting");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "Device is connected");
                        break;
                }

            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) { //Stores the device in the array
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBLDevice.add(device);
                mDeviceAdapter = new DeviceAdapter(context, R.layout.device_adapter_activity, mBLDevice);
                listDeviceView.setAdapter(mDeviceAdapter);

            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) { //if bonded already
                    mBTDevice=mDevice;
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) { //if making a bond
                    //nothing really
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) { //no bonds
                    //nothing really
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestriy:true");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnDiscover = (Button) findViewById(R.id.btnDiscover);
        Button btnONFF = (Button) findViewById(R.id.onoffBTN);
        listDeviceView = (ListView) findViewById(R.id.listDeviceView);
        mBLDevice = new ArrayList<>();

        btnStartConnection= (Button) findViewById(R.id.btnStartConnection);
        btnSend=(Button) findViewById(R.id.btnSend);
        chatText=(EditText) findViewById(R.id.chatText);

        incomingMessages = (TextView) findViewById(R.id.incomingMessage);
        messages = new StringBuilder();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("incomingMessage"));

        IntentFilter newintentFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, newintentFilter);

        listDeviceView.setOnItemClickListener(ChatActivity.this);

        btnONFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickListenerClicked");
                enableDisableBT();
            }

        });

        btnStartConnection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startConnection();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener(){//gets text and sends it
            @Override
            public void onClick(View vie){

                // Se crea un archivo
                PackageManager m = getPackageManager();
                String s = getPackageName();
                File file = new File(null);
                try {
                    PackageInfo p = m.getPackageInfo(s, 0);
                    s = p.applicationInfo.dataDir;
                } catch (PackageManager.NameNotFoundException e) {

                }
                try {
                    File file = new File(s+"/newfile.txt");

                    if (file.createNewFile()){
                        Toast.makeText(getApplicationContext(), "File Created at "+s,
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "File is already present at "+s,
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Couldn't create file on directory "+s,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                //Obtengo en bytes lo quiere mandar
                //byte[] bytes=chatText.getText().toString().getBytes(Charset.defaultCharset());
                byte[] bytes= file;
                //Se lo paso a mandar por la conexion
                mBluetoothConnection.write(bytes);
                chatText.setText("");
            }
        });
    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            /*PackageManager m = getPackageManager();
            String s = getPackageName();
            try {
                PackageInfo p = m.getPackageInfo(s, 0);
                s = p.applicationInfo.dataDir;
            } catch (PackageManager.NameNotFoundException e) {

            }
            try {
                File file = new File(s+"/newfile.txt");

                if (file.createNewFile()){
                    Toast.makeText(getApplicationContext(), "File Created at "+s,
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "File is already present at "+s,
                            Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Couldn't create file on directory "+s,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }*/
            String text = intent.getStringExtra("theMessage");
            messages.append(text + "\n");
            incomingMessages.setText(messages);
        }
    };

    public void generarArchivo(Context context, String sFileName, String sBody) {

        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {

        }

        try {
            File root = new File(s + "/" + sFileName);
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnection.startClient(device, uuid);

    }


    public void enableDisableBT() {
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }


    public void btnDiscover(View view) {
        Log.d(TAG, "btn discoverable created for a few seconds");

        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); //defines discoverable duration
        startActivity(discoverable);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }


    public void btnLookup(View view) {

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();

            IntentFilter lookupFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, lookupFilter);
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter lookupFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, lookupFilter);
        }

    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();//discovery is memory intensive
        String devicename= mBLDevice.get(i).getName();
        String deviceaddress= mBLDevice.get(i).getAddress();
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBLDevice.get(i).createBond();

            mBTDevice=mBLDevice.get(i);
            mBluetoothConnection=new BluetoothConnection(ChatActivity.this); //starts connection
            }
        }
}


