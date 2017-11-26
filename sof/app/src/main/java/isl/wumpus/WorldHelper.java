package isl.wumpus;

/**
 * Created by Mariafer on 28/10/2017.
 */


import android.content.Context;
import android.location.Location;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.beyondar.android.opengl.util.LowPassFilter;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Esta clase maneja las reglas de los objetos del juego
 */
public class WorldHelper {
    Context contextM;
    public static World world;
    ArrayList<GeoObject> listaGeoObject = new ArrayList<>();
    ArrayList<GeoObject> listaGeoMonstruos = new ArrayList<>();

    int cuevaActual;
    int contCuevas;
   ArrayList<Integer>cuevasAdyacentes = new ArrayList<>();; //cuevas adyacentes a la actual
    int[] caminosA;
    int[] caminosB;
    int cuevaWumpus=-1;
    int cuevaPozo=-1;
    int cuevaMurcielago=-1;
    //private Ubicacion ubicacion;
    public int getCuevaWumpus(){
        return cuevaWumpus;
    }
    public int getCuevaPozo(){
        return cuevaPozo;
    }
    public int getCuevaMurcielago(){
        return cuevaMurcielago;
    }

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
        contextM = context;
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

        GeoObject gm;
        gm = new GeoObject(contCuevas+1);
        gm.setName("objectMurcielago");
        gm.setDistanceFromUser(2);
        gm.faceToCamera(true);
        gm.setImageResource(R.drawable.murcielago);
       gm.setGeoPosition(listaGeoObject.get(1).getLatitude(),listaGeoObject.get(1).getLongitude());
        gm.setVisible(false);
        listaGeoMonstruos.add(gm);
        world.addBeyondarObject(gm); //agrega el objecto al RA


        asignarCaracteristicasCuevas();
        mostrarCuevas(cA, cB);
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
            while (cuevaMurcielago == -1) {
                if (temp >contCuevas) {
                    temp = temp - 3;
                }
                if (temp != cuevaWumpus && temp != cuevaPozo) {
                    cuevaMurcielago = temp;
                }
                temp++;
            }
            System.out.print(cuevaMurcielago+ " "+ cuevaPozo + " "+ cuevaWumpus);

        }

    }

    public void monstruoVisible(char tipo,boolean b){
        if(tipo=='m'){
            listaGeoMonstruos.get(0).setVisible(b);
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
        if(cuevaActual!=getCuevaWumpus() && cuevaActual!=getCuevaMurcielago() && cuevaActual!=getCuevaPozo() )
            for(int i: cuevasAdyacentes){
            int ad = i+1;
            if(ad == getCuevaMurcielago()){
                MediaPlayer mediaPlayer = MediaPlayer.create(contextM, R.raw.murcielago);
                mediaPlayer.start();
                Toast toast2 = Toast.makeText(contextM, "¿Qué pudo haber causado ese chirrido?", Toast.LENGTH_LONG);
                toast2.show();
            }
            if(ad == getCuevaWumpus()){
                Toast toast1 = Toast.makeText(contextM, "¡¡¡Siento la precencia del WUMPUS, emite un hedor imperdible !!!!", Toast.LENGTH_LONG);
                toast1.show();
            }

            if(ad == getCuevaPozo()){
                Toast toast3 = Toast.makeText(contextM, "Siento una brisa helada que me congela hasta los huesos....", Toast.LENGTH_LONG);
                toast3.show();
            }
        }

    }








    }





