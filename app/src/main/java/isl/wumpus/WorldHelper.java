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

/**
 * Esta clase maneja las reglas de los objetos del juego
 */
public class WorldHelper {
    public static World world;
    ArrayList<GeoObject> listaGeoObject = new ArrayList<>();
    int cuevaActual;
    int contCuevas;
   ArrayList<Integer>cuevasAdyacentes = new ArrayList<>();; //cuevas adyacentes a la actual
    int[] caminosA;
    int[] caminosB;
    int cuevaWumpus=0;
    int cuevaPozo=0;
    int cuevaMurcielago=0;
    //private Ubicacion ubicacion;

    /**
     * Metodo que crea los objetos del mundo
     * @param context
     * @param latlngArray coordenadas de las cuevas
     * @param cA vector de caminos
     * @param cB vector de caminos
     * @return
     */
    public World createWorld(Context context, ArrayList<LatLng> latlngArray,int[] cA,int[] cB){ //recibe array con cordenadas
        //Si ya existe un mundo
        contCuevas = latlngArray.size();
        if(world != null){
            return world;
        }
        //Creamos el mundo
        world = new World(context);
       // ubicacion = new Ubicacion(context);
        createObjects(latlngArray,cA,cB);
        // LowPassFilter.ALPHA = 1; para arreglar que los geo objects se mueven mucho
        return world;
    }

    /**
     * Método para crear objetos georeferenciados y agregarlos al mundo de RA
     * Crea las cuevas y demas objetos necesarios para el juego
     * @param latlngArray es un array de LatIng que contiene las coordenadas de las cuevas
     */
    public void createObjects(ArrayList<LatLng> latlngArray,int[] cA,int[] cB) {
        //iterar el locations
        GeoObject go;
        int i = 0;
        for (LatLng l : latlngArray) {
            go = new GeoObject(i++);
            go.setGeoPosition(l.latitude, l.longitude);
            go.setName("" + i);
            go.setImageResource(R.mipmap.cueva8bit);
            go.setVisible(false);
            listaGeoObject.add(go);
            world.addBeyondarObject(go); //agrega el objecto al RA
        }
        cuevaActual = 1;
        mostrarCuevas(cA, cB);
        asignarCaracteristicasCuevas();
    }

    /**
     * Este metodo asigna los murcielagos , el wumpus y los pozos a las cuevas
     */
    public void asignarCaracteristicasCuevas(){
        //la cueva 1 siempre estara vacia
        cuevaWumpus = (int) (Math.random() * contCuevas - 1) + 2; // rango entra 2 y cantidad de cuevas
        if(contCuevas >3) {
            int temp = (int) (Math.random() * contCuevas - 1) + 2;
            if (temp != cuevaWumpus) {
                cuevaPozo = temp;
            } else {
                cuevaPozo = temp + 1;
            }
            temp = (int) (Math.random() * contCuevas - 1) + 2;
            while (cuevaMurcielago == 0) {
                if (temp == contCuevas) {
                    temp = temp - 2;
                }
                if (temp != cuevaWumpus && temp != cuevaPozo) {
                    cuevaMurcielago = temp;
                }
                temp++;
            }
        }

    }

    public int getCuevaActual(){
        return cuevaActual;
    }

    /**
     * Este metodo cambia la cueva actual y llama a mostrar cuevas para actualizar las cuevas adyacentes a la actual
     * @param i nueva cueva actual
     * @param ca vector de caminos
     * @param cb vector de caminos
     */
    public void setCuevaActual(int i,int [] ca, int[] cb){
        cuevaActual=i;
        mostrarCuevas(ca,cb);
    }

    /**
     * Este metodo define las cuevas adyacentes a la cueva actual , solo estas cuevas se mostraran en la pantalla
     */
    public void mostrarCuevas(int[] caminosA, int[] caminosB) {
        //si alguno de los puntos es la cueva actual, haga visible el otro punto(cueva)
        // pone todas las cuevas invisibles
        for (int i =0;i<listaGeoObject.size();i++){
            listaGeoObject.get(i).setVisible(false);
        }
        for (int i = 0; i < caminosA.length; i++) {
            //En lista objects los objetos empiezan en la posicion 0, no existe el numero de cueva 0 en caminosA y caminos B
            if (caminosA[i] == cuevaActual || caminosB[i] == cuevaActual) {
                if (caminosA[i] == cuevaActual) {
                    listaGeoObject.get(caminosB[i]-1).setVisible(true);
                    cuevasAdyacentes.add(caminosB[i]-1); //esta es la posicion de la cueva en listaGeoObject

                } else {
                    listaGeoObject.get(caminosA[i]-1).setVisible(true);
                    cuevasAdyacentes.add(caminosA[i]-1); //esta es la posicion de la cueva en listaGeoObject
                }
            }
        }

        //aqui se puede compbrobar cual es la lista de adyacentes
        for(int i: cuevasAdyacentes){
            int ad = i;
        }

    }








    }





