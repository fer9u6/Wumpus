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


    //private static Context ctx;

    /*public GestionadorDeArchivos(Context c) {
        this.ctx = c;
    }*/

    //Convierte objeto a string
    public static String convertirObjetoAString(Object o){
        final Gson gson = new Gson();
        // 1. Java object to JSON,
        //return gson.toJson(o).toString();
        return gson.toJson(o);
    }

    //Convierte String a objeto
    public static Object convertirStringAObjeto(String string){
        final Gson gson = new Gson();
        Object o = gson.fromJson(string, Object.class);
        return o;
    }

    //Recibe el nombre de laberinto que quiere abrir
    public static String read(String nombrelab/*, View view*/, Context ctx) {
        try {
            FileInputStream fileInputStream= ctx.getApplicationContext().openFileInput(nombrelab+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }
            return stringBuffer.toString();
            //view.texto.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void write(String nombrelab, String string, Context ctx) {


        File folder = new File(ctx.getFilesDir() + File.separator + "Laberintos");
        if(!folder.exists()){
            folder.mkdir();
        }


        File file = new File(ctx.getFilesDir() + File.separator + "Laberintos" + File.separator + nombrelab+".mapa");
        FileOutputStream fos;

        try{
            if(!file.exists()){
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            fos.write(string.getBytes());
            fos.close();

        }catch(IOException e){
            e.printStackTrace();
        }


            /*
            FileOutputStream fileOutputStream = ctx.openFileOutput(nombrelab + ".mapa",MODE_PRIVATE);
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();*/
        Toast.makeText(ctx.getApplicationContext(),"Laberinto guardado como "+ctx.getFilesDir() + File.separator + "Laberintos" + File.separator + nombrelab+".mapa",Toast.LENGTH_LONG).show();
    }


}