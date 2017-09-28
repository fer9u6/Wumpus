package isl.wumpus;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;


/**
 * Created by a75144 on 27/09/2017.
 */

public class GestionadorDeArchivos {

    //File file = new File(/*context.getFilesDir(), filename*/"archivo");


     void guardarArchivoInterno(String stringAGuardar, String fileName, Context ctx){
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
            Toast.makeText(this, "Hubo un problema al guardar",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
    }



    public void read(View view, Context ctx) {
        try {
            FileInputStream fileInputStream= ctx.getApplicationContext().openFileInput("myText.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }
            //textView.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(View view) {
        String Mytextmessage  = editText.getText().toString();
        try {
            FileOutputStream fileOutputStream = openFileOutput("myText.txt",MODE_PRIVATE);
            fileOutputStream.write(Mytextmessage.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"Text Saved",Toast.LENGTH_LONG).show();
            editText.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}