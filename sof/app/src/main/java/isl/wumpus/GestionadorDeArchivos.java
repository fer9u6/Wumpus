package isl.wumpus;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.in;
import static java.security.AccessController.getContext;


/**
 * Created by a75144 on 27/09/2017.
 */

public class GestionadorDeArchivos {


    /**
     * Convierte un Objeto de tipo Mapa a String de Json unando Gson
     * @param o Objeto Mapa que se convierte en string
     * @return String que contiene los atributos del objeto mpapa.
     */
    public static String convertirObjetoAString(Mapa o){
        final Gson gson = new Gson();
        return gson.toJson(o);
    }

    /**
     * Convierte un String de Json a un objeto de tipo Mapa
     * @param string string que se desea convertir a Mapa
     * @return Objeto Mapa
     */
    public static Mapa convertirStringAObjeto(String string){
        final Gson gson = new Gson();
        Mapa o = gson.fromJson(string, Mapa.class);
        return o;
    }

    /**
     * Lee de memoria interna /Mapas un archivo que contiene un String.
     * @param nombrelab String que contiene el nombre sin extensión del archivo que se quiere leer.
     * @param ctx Contexto del activity que llama al método.
     * @return String que está contenido en el archivo.
     */
    public static String read(String nombrelab, Context ctx) {
        try {
            FileInputStream fileInputStream= new FileInputStream(new File(ctx.getFilesDir() + File.separator + "Mapas" + File.separator +nombrelab+".mapa"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Escribe en memoria interna un archivo que contiene un String
     * @param nombrelab el nombre del archivo a escribirse, sin extensión.
     * @param ctx Contexto del activity que llama al método.
     */
    public static void write(String nombrelab, String string, Context ctx) {

        File folder = new File(ctx.getFilesDir() + File.separator + "Mapas");
        if(!folder.exists()){
            folder.mkdir();
        }
        File file = new File(folder, nombrelab+".mapa");
        FileOutputStream fos;

        try{
            fos = new FileOutputStream(file);
            fos.write(string.getBytes());
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        Toast.makeText(ctx.getApplicationContext(),"Laberinto guardado como "+ file.getAbsolutePath(),Toast.LENGTH_LONG).show();
    }

}