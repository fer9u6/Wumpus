package isl.wumpus;

/**
 * Created by Mariafer on 28/10/2017.
 */

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;


public class RealidaAumentada extends FragmentActivity implements OnClickBeyondarObjectListener {
    private BeyondarFragmentSupport mBeyondarFragment;
    private WorldHelper worldHelper;
    private World mWorld;
    ArrayList<LatLng> latlngArray;



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
                }
            }

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);



        worldHelper = new WorldHelper();

        //Esto permite que BeyondAR pueda acceder a la posición del usuario
        BeyondarLocationManager.setLocationManager((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));

        //Creo el mundo
        mWorld = worldHelper.createWorld(this, latlngArray); //mandar array de locations

        /*Parametros para variar la distancia de los objetos*/
        mBeyondarFragment.setMaxDistanceToRender(3000); //Asigno distancia máxima de renderización de objetos
        mBeyondarFragment.setDistanceFactor(4); //El factor de distancia de objetos (más cerca entre mayor valor)
        mBeyondarFragment.setPushAwayDistance(4); //Para alejar un poco los objetos que están muy cerca
        mBeyondarFragment.setPullCloserDistance(4); //Para acercar un poco los objetos que están muy lejos //3
        mBeyondarFragment.setWorld(mWorld);

        //BeyondarLocationManager.enable();


        //Permitimos que BeyondAR actualice automáticamente la posición del mundo con respecto al usuario
        BeyondarLocationManager.addWorldLocationUpdate(mWorld);

        // Le pasamos el LocationManager al BeyondarLocationManager.
        BeyondarLocationManager
                .setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        //Si queremos implementar algun evento al tocar un objeto de RA
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);
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

    /**
     * Método para registar el evento de tap a algún objeto del mundo RA
     * @param arrayList contiene el objeto georefernciado
     */
    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> arrayList) {
        // The first element in the array belongs to the closest BeyondarObject
        Toast.makeText(this, "Clicked on: " + arrayList.get(0).getName(), Toast.LENGTH_LONG).show();
    }
}
