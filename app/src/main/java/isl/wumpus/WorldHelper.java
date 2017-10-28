package isl.wumpus;

/**
 * Created by Mariafer on 28/10/2017.
 */


import android.content.Context;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

public class WorldHelper {
    public static World world;
    //private Ubicacion ubicacion;

    /**
     * Método para crear el mundo de RA
     * @param context es el contexto donde se encuentra la aplicación
     * @return
     */
    public World createWorld(Context context){
        //Si ya existe un mundo
        if(world != null){
            return world;
        }
        //Creamos el mundo
        world = new World(context);
       // ubicacion = new Ubicacion(context);
        createObjects();
        return world;

    }

    /**
     * Método para crear objetos georeferenciados y agregarlos al mundo de RA
     */
    public void createObjects(){
        /*Posicion estática para colocar al mundo en algun punto. Si se desea
        * utilizar la ubicación actual, comentar esto y en MainActivity descomentar
        * BeyondarLocationManager.enable() en el onCreate*/
        world.setGeoPosition(9.956388, -84.171513);

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


    }




}
