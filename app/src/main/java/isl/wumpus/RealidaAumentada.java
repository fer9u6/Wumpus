package isl.wumpus;

/**
 * Created by Mariafer on 28/10/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;

/**
 * Clase que puede despliega los objetos de la realidad aumentada y captura los eventos de los objetos
 */
public class RealidaAumentada extends FragmentActivity implements OnClickBeyondarObjectListener {
    private BeyondarFragmentSupport mBeyondarFragment;
    private WorldHelper worldHelper;
    private World mWorld;
    ArrayList<LatLng> latlngArray;
    int[] caminoA;
    int[] caminoB;
    int paraTextCuevaActual;
    boolean modoEntrarACueva;
    private TextView textView;
    private ImageView IVmurcielago;
    private ImageView imagenCueva;

    /**
     * Metodo que inicializa las atributos principales de la ralidad aumentada y envia como parametro las coordenadas de las cuevas
     * a createObjects.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.beyondar);
        latlngArray = new ArrayList<>();



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                latlngArray= null; //nunca seria null
            } else {
                  latlngArray = getIntent().getParcelableArrayListExtra("latlngArray");//Recibe el ArrayListd latlngArray de Emplazar
                caminoA = getIntent().getIntArrayExtra("caminosA");
                caminoB= getIntent().getIntArrayExtra("caminosB");
               }
            }

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        textView = (TextView)findViewById(R.id.textView);
        IVmurcielago = (ImageView)findViewById(R.id.viewMurcielago) ;

        imagenCueva = (ImageView)findViewById(R.id.imagenCueva) ;
        imagenCueva.setVisibility(View.VISIBLE);


        worldHelper = new WorldHelper();

        //Esto permite que BeyondAR pueda acceder a la posición del usuario
        BeyondarLocationManager.setLocationManager((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));

        //Creo el mundo
        mWorld = worldHelper.createWorld(this, latlngArray,caminoA,caminoB); //mandar array de locations

        /*Parametros para variar la distancia de los objetos*/
        mBeyondarFragment.setMaxDistanceToRender(3000); //Asigno distancia máxima de renderización de objetos
        mBeyondarFragment.setDistanceFactor(0); //El factor de distancia de objetos (más cerca entre mayor valor)
        mBeyondarFragment.setPushAwayDistance(7); //Para alejar un poco los objetos que están muy cerca
        mBeyondarFragment.setPullCloserDistance(0); //Para acercar un poco los objetos que están muy lejos //3
        mBeyondarFragment.setWorld(mWorld);

        BeyondarLocationManager.enable();


        //Permitimos que BeyondAR actualice automáticamente la posición del mundo con respecto al usuario
        BeyondarLocationManager.addWorldLocationUpdate(mWorld);

        // Le pasamos el LocationManager al BeyondarLocationManager.
        BeyondarLocationManager
                .setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        //Si queremos implementar algun evento al tocar un objeto de RA
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);
        textView.setText("Cueva actual: "+  worldHelper.getCuevaActual());
    }

    @Override
    protected void onResume(){
        // Enable GPS
        super.onResume();
        BeyondarLocationManager.enable();
    }

    @Override
    protected void onPause(){
        // Disable GPS
        super.onPause();
        BeyondarLocationManager.disable();
    }

    public void mostrarMurcielago(boolean r){
        if(r==true) {
            IVmurcielago.setVisibility(View.VISIBLE);
        }else{
            IVmurcielago.setVisibility(View.INVISIBLE);
        }
    }

    public void mostrarCuevaActual(){
        Toast.makeText(this, "Cueva Actual:"+worldHelper.getCuevaActual(), Toast.LENGTH_LONG).show();
    }

    /**
     * Método para registar el evento de tap a algún objeto del mundo RA y muestra el nombre del objeto(numero de cueva)
     * @param arrayList contiene el objeto georefernciado
     */
    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> arrayList) {

        final int idcueva = (int) arrayList.get(0).getId();
        //Si la cueva esta a 5m, puede entrar. Sino, mostrar distancia.
        //if (arrayList.get(0).getDistanceFromUser() > 5){
         //   Toast.makeText(this, "La cueva "+(idcueva + 1)+" está a más de 5 metros. Distancia: "+(int)arrayList.get(0).getDistanceFromUser()+" metros.", Toast.LENGTH_LONG).show();
       // } else {
             //worldHelper.monstruoVisible('m',false);
              mostrarMurcielago(false);
            AlertDialog.Builder entrarACueva = new AlertDialog.Builder(this);
            entrarACueva.setTitle("Entrar en cueva");
            entrarACueva.setMessage("¿Desea entrar a la cueva " + (idcueva + 1) + "?");
            entrarACueva.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //idCueva +1 porque en los objetos si existe la cueva 0 , pero en los caminos no existe la cueva 0

                    worldHelper.setCuevaActual(idcueva + 1, caminoA, caminoB);
                    int cuevaActual = worldHelper.getCuevaActual();
                    mostrarCuevaActual();
                    textView.setText("Cueva actual: "+ worldHelper.getCuevaActual());
                    if(worldHelper.getCuevaWumpus()==cuevaActual){
                        Intent in = new Intent(getApplicationContext(), activity_perdioJuego.class);
                        startActivity(in);
                    }
                    if(worldHelper.getCuevaMurcielago()==cuevaActual){
                        //worldHelper.monstruoVisible('m',true);
                        mostrarMurcielago(true);

                    }
                    if(worldHelper.getCuevaPozo()==cuevaActual){
                        Intent ia = new Intent(getApplicationContext(), caer_en_pozo.class);
                        startActivity(ia);
                    }
                    textView.setText("Cueva actual: "+ worldHelper.getCuevaActual());
                }
            });
            entrarACueva.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            entrarACueva.show();


       // }
    }
    }

