package isl.wumpus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CargarLaberintoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_laberinto);



    }

    //Genera poliedros regulares en memoria
    public void generarPoliedros() {
        GestionadorDeArchivos ga = new GestionadorDeArchivos();

        for (int i = 0; i <= 4 ; i++ ){
        }

        //Si no existe el archivo TXT del poliedro regular
        if (ga.read("tetrahedron",this) == ""){

           // Mapa m = new Mapa ()

        }

    }
}
