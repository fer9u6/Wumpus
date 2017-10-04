package isl.wumpus;

import android.app.Activity;
import android.content.Context;
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


    //File file = new File(/*context.getFilesDir(), filename*/"archivo");


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



    /*
     public void guardarArchivoInterno(String stringAGuardar, String fileName, Context ctx){
        FileOutputStream fos = null;
        try{
            fos = ctx.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(stringAGuardar.getBytes());
            fos.close();
            //save preference
            preferenceEditor.putBoolean("guardado interno", true);
            //show button & listener
            buttonShowInternal.setVisibility(View.VISIBLE);
            buttonShowInternal.setOnClickListener(this);
        }catch (IOException e){
           // Toast.makeText(this, "Hubo un problema al guardar",
           //         Toast.LENGTH_SHORT).show();
           // e.printStackTrace();
        }
    }

    public void guardarArchivo(Context ctx) {
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            //llamo a context con getContext() o getApplicationContext() o this
            outputStream = ctx.openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    static MainActivity ctx;

   public static void main(String args[]){
        /*Se crea un String "prueba" en el main, se convierte a objeto
          y se reconvierte a String para luego imprimir en consola
        */

        String a = "prueba";
        Object o = convertirStringAObjeto(a);

        System.out.println(convertirObjetoAString(o));
        write("prueba",convertirObjetoAString(o),ctx);
   }

    //Recibe el nombre de laberinto que quiere abrir
    public static String read(String nombrelab, View view, Context ctx) {
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
            //textView.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void write(String nombrelab, String string, Context ctx) {
        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput(nombrelab + ".txt",MODE_PRIVATE);
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();
            Toast.makeText(ctx.getApplicationContext(),"Se guardó el laberinto",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}