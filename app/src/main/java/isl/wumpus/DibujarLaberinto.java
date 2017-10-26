package isl.wumpus;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.InputType;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class DibujarLaberinto extends AppCompatActivity implements View.OnClickListener {

    private static Lienzo lienzo;
    public Button bCueva;
    public Button bCamino;
    public Button bGuardar;
    public Button bBorrar;
    public Button bPoliedro;
    public Button bEmplazar;
    public Mapa mapa;
    private String nombre = ""; //input nombre laberinto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujar_laberinto);
        bCamino = (Button) findViewById(R.id.btncamino);
        bCueva = (Button) findViewById(R.id.btncueva);
        bGuardar = (Button) findViewById(R.id.btnguardar);
        bBorrar =(Button) findViewById(R.id.btnborrar);
        bPoliedro=(Button) findViewById(R.id.btnPoliedro);
        bEmplazar =(Button)findViewById(R.id.btnEmplazar);
        lienzo = (Lienzo) findViewById(R.id.lienzo);
        bCamino.setOnClickListener(this);
        bPoliedro.setOnClickListener(this);
        bCueva.setOnClickListener(this);
        bGuardar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        bEmplazar.setOnClickListener(this);

    }

    public void guardarLaberinto(){
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(this);
        salvarDibujo.setTitle("Salvar laberinto");
        salvarDibujo.setMessage("¿Salvar Laberinto?");
        salvarDibujo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                //Salvar dibujo
                lienzo.setDrawingCacheEnabled(true);

                //Para asociar PNG con TXT

                File folder = new File(getFilesDir() + File.separator + "Imagenes");

                if(!folder.exists()){
                    folder.mkdir();
                }

                File archivoimagen=new File(folder,nombre+".png");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(archivoimagen);
                    lienzo.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getApplicationContext(),"Imagen guardada como "+ archivoimagen.getAbsolutePath(),Toast.LENGTH_LONG).show();
                GestionadorDeArchivos ga = new GestionadorDeArchivos();
                ga.write(nombre,ga.convertirObjetoAString(mapa),getApplicationContext());

                lienzo.destroyDrawingCache();


            }
        });
        salvarDibujo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        salvarDibujo.show();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncueva:
                lienzo.modo(true);
                if(bCueva.getText().equals("NuevaCueva")) {
                    lienzo.nuevaCueva();
                }
                bCueva.setText("NuevaCueva");
                break;

            case R.id.btncamino:
                lienzo.modo(false);
                bCueva.setText("Cueva");
                break;
            case R.id.btnguardar:
                lienzo.modo(true);
                mapa = new Mapa(lienzo.cuevaX, lienzo.cuevaY, lienzo.caminoA,
                        lienzo.caminoB, lienzo.numCuevas, lienzo.contadorCamino);
                if(mapa.Validar()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Nombre del laberinto:");

                    final EditText input = new EditText(this);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nombre = input.getText().toString();
                            guardarLaberinto();

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                } else Toast.makeText(getApplicationContext(), "Mapa invalido.", Toast.LENGTH_LONG).show();

                break;
            case R.id.btnborrar:
                lienzo.borrar();
                break;
            case R.id.btnPoliedro:
                lienzo.crearDodecahedro();
                break;
            case R.id.btnEmplazar:
                if(!nombre.equals("")) {
                    Intent i = new Intent(this, EmplazarMapa.class);
                    i.putExtra("nM", nombre);
                    startActivity(i);
                } else Toast.makeText(getApplicationContext(), "Mapa invalido.", Toast.LENGTH_LONG).show();
        }

    }

}
