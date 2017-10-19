package isl.wumpus;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.File;
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
        bEmplazar =(Button)findViewById(R.id.btnEmplazar);
        lienzo = (Lienzo) findViewById(R.id.lienzo);
        bCamino.setOnClickListener(this);
        bCueva.setOnClickListener(this);
        bGuardar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        bEmplazar.setOnClickListener(this);

       /* File fileDir = new File(getFilesDir().getAbsolutePath());
       fileDir.mkdirs();*/

    }

    public void guardarLaberinto(){
        AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(this);
        salvarDibujo.setTitle("Salvar laberinto");
        salvarDibujo.setMessage("¿Salvar Laberinto a la galeria?");
        salvarDibujo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                //Salvar dibujo
                lienzo.setDrawingCacheEnabled(true);

                //Para asociar PNG con TXT. Pedir nombre?


                //attempt to save
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), lienzo.getDrawingCache(),
                        nombre+".png", "drawing");

                //Guardar
                GestionadorDeArchivos ga = new GestionadorDeArchivos();
                ga.write(nombre,ga.convertirObjetoAString(mapa),getApplicationContext());


                //Mensaje de todo correcto
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "¡Laberinto salvado en la galeria!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "¡Error! El laberinto no ha podido ser salvado.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
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

                    //AlertDialog Nombre del laberinto.
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

            case R.id.btnEmplazar:
                if(mapa.Validar()) {
                    Intent i = new Intent(this, EmplazarMapa.class);
                    startActivity(i);
                } else Toast.makeText(getApplicationContext(), "Mapa invalido.", Toast.LENGTH_LONG).show();
        }

    }

}
