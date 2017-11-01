package isl.wumpus;

/**
 * Created by Mariafer on 28/10/2017.
 */


import android.content.Context;
import android.location.Location;

import com.beyondar.android.opengl.util.LowPassFilter;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class WorldHelper {
    public static World world;
    //private Ubicacion ubicacion;

    /**
     * Método para crear el mundo de RA
     * @param context es el contexto donde se encuentra la aplicación
     * @return
     */
    public World createWorld(Context context, ArrayList<LatLng> latlngArray ){ //recibe array con cordenadas
        //Si ya existe un mundo
        if(world != null){
            return world;
        }
        //Creamos el mundo
        world = new World(context);
       // ubicacion = new Ubicacion(context);
        createObjects(latlngArray);

        // LowPassFilter.ALPHA = 1; para arreglar que los geo objects se mueven mucho

        return world;

    }

    /**
     * Método para crear objetos georeferenciados y agregarlos al mundo de RA
     */
    public void createObjects(ArrayList<LatLng> latlngArray){
        /*Posicion estática para colocar al mundo en algun punto. Si se desea
        * utilizar la ubicación actual, comentar esto y en MainActivity descomentar
        * BeyondarLocationManager.enable() en el onCreate*/

        //iterar el locations
        ArrayList<GeoObject> listaGeoObject = new ArrayList<>();
        GeoObject go;
        int i = 0;
        for (LatLng l:latlngArray){
            go = new GeoObject(i++);
            go.setGeoPosition(l.latitude, l.longitude);
            go.setName(""+i);
            go.setImageResource(R.mipmap.cueva);
            listaGeoObject.add(go);
            world.addBeyondarObject(go); //agrega el objecto al RA
        }

        world.setGeoPosition(latlngArray.get(0).latitude,latlngArray.get(0).longitude);
        //world.setGeoPosition(9.956388, -84.171513);
        /*
        GeoObject geo1 = new GeoObject(1);
        geo1.setGeoPosition(9.957452, -84.170577);
        geo1.setName("Soy el monstruo azul");
        geo1.setImageResource(R.drawable.creature_tulu);

        GeoObject geo2 = new GeoObject(2);
        geo2.setGeoPosition(9.957346, -84.171264);
        geo2.setName("Soy el monstruo naranja");
        geo2.setImageResource(R.drawable.creature_tulu);

        GeoObject geo3 = new GeoObject(3);
        geo3.setGeoPosition(9.956081, -84.171679);
        geo3.setName("Soy el monstruo verde");
        geo3.setImageResource(R.drawable.creature_muck);

        GeoObject geo4 = new GeoObject(4);
        geo4.setGeoPosition(9.956416, -84.171725);
        geo4.setName("Soy el monstruo morado");
        geo4.setImageResource(R.drawable.creature_muck);

        world.addBeyondarObject(geo1);
        world.addBeyondarObject(geo2);
        world.addBeyondarObject(geo3);
        world.addBeyondarObject(geo4);
        */

    }




}
