package isl.wumpus;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The type Chat activity.
 */
public class ChatActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActiviy";
    /**
     * mBluetoothAdapter: Obtiene el adaptador de bluetooth
     */
    BluetoothAdapter mBluetoothAdapter;
    /**
     * btnDiscover: Boton para la funcion de descubrir dispositivo
     */
    Button btnDiscover;

    /**
     * btnStartConnection: Llama a la funcion de conexion
     */
    Button btnStartConnection;
    /**
     * btnSend: Llama a la funcion que envia archivos
     */
    Button btnSend;

    /**
     * incomingMessages: Muestra mensajes en pantalla
     */
    TextView incomingMessages;
    /**
     * messages: El mensaje enviado, se usa para convertir de String a StringBuilder
     */
    StringBuilder messages;

    /**
     * mBluetoothConnection: Maneja las conexiones de bluetooth, requiere de UUID
     */
    BluetoothConnection mBluetoothConnection;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    /**
     * mBTDevice: Usa a device adapter
     */
    BluetoothDevice mBTDevice;

    /**
     * chatText: El nombre del archivo a enviar
     */
    EditText chatText;

    /**
     * mBLDevice: Un array con todos los dispositivos que se pueden conectar
     */
    public ArrayList<BluetoothDevice> mBLDevice = new ArrayList<>();
    /**
     * mDeviceAdapter: De la clase DeviceAdapter
     */
    public DeviceAdapter mDeviceAdapter;
    /**
     * listDeviceView: Muestra los dispositivos que se pueden conectar
     */
    ListView listDeviceView;

    /**
     * line: Donde se almacena el mensaje recibido.
     */
    String line;

    /*
    *BroadcastReceiver: Lee los cambios en ACTION_STATE, el estado del bluetooth en el mobil
    */

    MediaPlayer mediaPlayer;


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

   /*
   * mBroadcastReceiver2: Lee los cambios en la conexion
   */
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

    /*
    *mBroadcastReceiver3: Lee los cambios en los dispositivos disponibles
     */
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

    /*
    *mBroadcastReceiver4: Lee si se esta conectando o no un dispositivo
     */
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
        super.onDestroy();
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
            public void onClick(View view){

                // Se encuentra la ruta del archivo
                PackageManager m = getPackageManager();
                String s = getPackageName();
                try {
                    PackageInfo p = m.getPackageInfo(s, 0);
                    s = p.applicationInfo.dataDir;
                } catch (PackageManager.NameNotFoundException e) {

                }
                //Usa la ruta de nuestra aplicacion, chatText es el nombre a buscar
                String ruta = s+"/files/Mapas/"+chatText.getText().toString()+".mapa";
                File file = new File(ruta);

               //Contiene el texto dentro de .mapa
                String line2=null;
                try {
                    // FileReader, lector por defecto de Java
                    FileReader fileReader =
                            new FileReader(ruta);

                    //BufferedReader para leer las lineas
                    BufferedReader bufferedReader =
                            new BufferedReader(fileReader);

                    while((line = bufferedReader.readLine()) != null) {
                        Toast.makeText(getApplicationContext(), "Se leyo 1 "+ line,
                                Toast.LENGTH_SHORT).show();

                        line2 = line;

                        Toast.makeText(getApplicationContext(), "Leyendo el contenido del archivo",
                                Toast.LENGTH_SHORT).show();
                    }

                    //Cierra el archivo
                    bufferedReader.close();
                }
                //Error, el archivo no existe
                catch(FileNotFoundException ex) {
                  Toast.makeText(getApplicationContext(), "No se pudo abrir el archivo"+ruta,
                            Toast.LENGTH_SHORT).show();
                }

                //Error, no se puede leer el archivo
                catch(IOException ex) {

                    Toast.makeText(getApplicationContext(), "Error leyendo el archivo"+ruta,
                            Toast.LENGTH_SHORT).show();
                }
                System.out.println(line2);
                Toast.makeText(getApplicationContext(), "Line 2 es "+ line2,
                        Toast.LENGTH_SHORT).show();

                //Obtiene en bytes lo quiere mandar
                byte[] bytes=line2.getBytes(Charset.defaultCharset());
                //Se lo paso a mandar por la conexion
                mBluetoothConnection.write(bytes);
                chatText.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tema_bluetooth);
        mediaPlayer.start();
    }

    public void insopen(View v){
        Intent i = new Intent(this,BluetoothInstrucciones.class);
        startActivity(i);
    }

    /**
     * mReceiver:Recibe mensajes y los guarda en archivos .mapa
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String text = intent.getStringExtra("theMessage");
            messages.append(text + "\n");
            incomingMessages.setText(messages);

            PackageManager m = getPackageManager();
            String s = getPackageName();
            try {
                PackageInfo p = m.getPackageInfo(s, 0);
                s = p.applicationInfo.dataDir;
            } catch (PackageManager.NameNotFoundException e) {

            }

            String recibido=messages.toString();
            // Toast.makeText(getApplicationContext(),"Mensaje es: "+recibido,Toast.LENGTH_SHORT).show();
            //Usa la ruta de nuestra aplicacion, chatText es el nombre a buscar
            String ruta = "data/user/0/isl.wumpus/files/Mapas/nuevoMapa.mapa";
            Toast.makeText(getApplicationContext(),"Se creara archivo en "+ruta,Toast.LENGTH_SHORT).show();

            GestionadorDeArchivos ga= new GestionadorDeArchivos();
            ga.write("nuevoMapa",recibido,getApplicationContext());

        }
    };


    /**
     * startConnection: Inicia conexiones bluetooth.
     */
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * startBTConnection: Conecta a los dispositivos
     * @param device  device: El mobil a conectarse
     * @param uuid    uuid: La direccion uuid a usarse, statica, definida al inicio del programa
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnection.startClient(device, uuid);

    }


    /**
     * enableDisableBT: permite activar o desactivar el Bluetooth
     */
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


    /**
     * btnDiscover: permite descubrir el mobil a conexiones     *
     * @param view: La vista donde esta el boton, requerido en este tipo de metodos
     */
    public void btnDiscover(View view) {
        Log.d(TAG, "btn discoverable created for a few seconds");

        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); //defines discoverable duration
        startActivity(discoverable);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2, intentFilter);
    }


    /**
     * btnLookup: Busca por dispositivos bluetooth disponibles     *
     * @param view: Requerido para el uso de botones
     */
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

    /**
     * prueba: Comprueba que el archivo fue recibido. Se retirara en versiones posteriores
     * @param view: Requerido para el uso de botones
     */
    public void prueba(View view){
            PackageManager m = getPackageManager();
            String s = getPackageName();
            try {
                PackageInfo p = m.getPackageInfo(s, 0);
                s = p.applicationInfo.dataDir;
            } catch (PackageManager.NameNotFoundException e) {

            }

        try {
                String todo=s+"/files/Mapas/"+chatText.getText().toString()+".mapa";
                File file = new File(todo);

                if (file.createNewFile()){
                    Toast.makeText(getApplicationContext(), "Se creo el archivo "+todo+", revise el codigo",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "El archivo "+todo+ " ya existe, funciona",
                            Toast.LENGTH_SHORT).show();
                }
            String line2=null;
            try {
                // FileReader, lector por defecto de Java
                FileReader fileReader =
                        new FileReader(todo);

                //BufferedReader para leer las lineas
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    Toast.makeText(getApplicationContext(), "Se leyo 1 "+ line,
                            Toast.LENGTH_SHORT).show();

                    line2 = line;

                    Toast.makeText(getApplicationContext(), "Leyendo el contenido del archivo",
                            Toast.LENGTH_SHORT).show();
                }

                //Cierra el archivo
                bufferedReader.close();
            }
            catch(FileNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "No se pudo abrir el archivo"+todo,
                        Toast.LENGTH_SHORT).show();
            }

            //Error, no se puede leer el archivo
            catch(IOException ex) {

                Toast.makeText(getApplicationContext(), "Error leyendo el archivo"+todo,
                        Toast.LENGTH_SHORT).show();
            }
            System.out.println(line2);
            Toast.makeText(getApplicationContext(), "Line 2 es "+ line2,
                    Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Couldn't create file on directory "+s,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
    }




}