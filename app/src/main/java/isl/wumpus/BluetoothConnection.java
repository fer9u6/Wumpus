package isl.wumpus;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * BluetoothConnection: Maneja la conexion de bluetooth, es llamado por ChatActivity
 */

public class BluetoothConnection {
    private static final String appName="Test";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private AcceptThread mInsecureConnectionThread;

    private final BluetoothAdapter mBluetoothAdapter;
    /**
     * mContext: El contexto actual
     */
    Context mContext;
    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;
    /**
     * mProgressDialog: Muestra una ventana cuando se esta estableciendo una conexion
     */
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    /**
     * BluetoothConection: Inicia una nueva conexion de Bluetooth
     *
     * @param context: El contexto actual, requerido por el constructor
     */
    public BluetoothConnection(Context context) {
        mContext = context;
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mServerSocket;

        /**
         * AcceptThread: Thread que busca si se ha aceptado la conexion
         */
        public AcceptThread(){ //
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

            }catch (IOException e){

            }
            mServerSocket=tmp;
        }
        /*
        *run: Si la conexion es aceptada se corre el metodo de aceptacion
         */
        public void run(){ //accepts connection
            BluetoothSocket socket=null;
            try {
                socket = mServerSocket.accept(); //Waits for connection
            }catch (IOException e){

            }
            if(socket!=null){
                connected(socket,mDevice);
            }

        }

        /**
         * Cancel: Detiene la conexion
         */
        public void cancel(){ //closes thread
            try{
                mServerSocket.close();
            }catch (IOException e){

            }
        }
    }

    private class ConnectThread extends Thread{ //connects to a device
        private BluetoothSocket mSocket;

        /**
         * Thread de conexion, busca conexiones
         *
         * @param device: el dispositivo que desea conectarse
         * @param uuid: la direccion declarada con la que queremos conectarnos
         */
        public ConnectThread(BluetoothDevice device, UUID uuid){
            mDevice=device;
            deviceUUID=uuid;
        }
        public void run(){ //gets automatically called
            BluetoothSocket tmp = null;
            try {
                tmp=mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                //Nothing
            }
            mSocket=tmp;
            mBluetoothAdapter.cancelDiscovery();
            try {
                mSocket.connect();
            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    //nothing
                }
            }
            connected(mSocket,mDevice);
        }

        /**
         * cancel: Cierra este hilo
         */
        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) {

            }
        }
    }

    /**
     * Start: Inicia el proceso de conexion
     */
    public synchronized void start(){ //starts accept thread

        if(mConnectThread!=null){//cancel new connection attempts
            mConnectThread.cancel();
            mConnectThread=null;
        }
        if(mInsecureAcceptThread==null){
            mInsecureAcceptThread=new AcceptThread();
            mInsecureAcceptThread.start();
        }

    }

    /**
     * startClient: La conexion del lado del cliente (emisor) hacia el servidor (receptor)
     *
     * @param device: el dispositivo a conetarse
     * @param uuid:   la direccion a conectarnos
     */
    public void startClient (BluetoothDevice device, UUID uuid){
        mProgressDialog=ProgressDialog.show(mContext, "Connecting to other device","" +
                "Please wait",true);
        mConnectThread=new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutputStream;

        /**
         * ConnectedThread: Inicia el connected thread
         *
         * @param socket: El socket de conexion declarado como mSocket
         */
        public ConnectedThread(BluetoothSocket socket){
            mSocket=socket;
            InputStream tmpin=null;
            OutputStream tmpout=null;
            try {
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try {
                tmpin=mSocket.getInputStream();
                tmpout=mSocket.getOutputStream();
            } catch (IOException e) {
                //just stop
            }

            mInputStream=tmpin;
            mOutputStream=tmpout;

        }

        public void run(){
            byte[] buffer= new byte[1024]; //stores the stream
            int bytes; //bytes returned from read();

            while (true){//listen input stream
                try {
                    bytes= mInputStream.read(buffer);
                    String incomingmessage= new String(buffer,0,bytes);

                    Intent incomingMessageIntent = new Intent("incomingMessage");
                    incomingMessageIntent.putExtra("theMessage",incomingmessage);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMessageIntent);

                } catch (IOException e) {
                    //nothing boey
                    break;
                }

            }
        }

        /**
         * write: Metodo que escribe en bytes al servidor
         *
         * @param bytes: Los bytes del mensaje que queremos enviar
         */
        public void write(byte[] bytes){

            String text = new String (bytes, Charset.defaultCharset());
            try {
                mOutputStream.write(bytes);
            } catch (IOException e) {
                //hello
            }
        }

        /**
         * cancel: Cierra la conexion al cerrar el socket
         */
        public void cancel(){
            try{
                mSocket.close();
            }catch (IOException e){

            }
        }

    }

    private void connected(BluetoothSocket mSocket, BluetoothDevice mDevice){
        mConnectedThread=new ConnectedThread(mSocket);
        mConnectedThread.start();
    }

    /**
     * write: Le escribe al servidor de la conexion.
     *
     * @param out: los bytes obtenidos
     */
    public void write(byte[] out){
        ConnectedThread r;
        mConnectedThread.write(out);
    }

}
