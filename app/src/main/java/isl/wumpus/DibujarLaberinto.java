package isl.wumpus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class DibujarLaberinto extends AppCompatActivity implements View.OnClickListener {

    private static Lienzo lienzo;
    public Button bCueva;
    public Button bCamino;
    public Button bGuardar;
    public Mapa mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujar_laberinto);
        bCamino = (Button) findViewById(R.id.btncamino);
        bCueva = (Button) findViewById(R.id.btncueva);
        bGuardar = (Button) findViewById(R.id.btnguardar);
        lienzo = (Lienzo) findViewById(R.id.lienzo);
        bCamino.setOnClickListener(this);
        bCueva.setOnClickListener(this);
        bGuardar.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncueva:
                lienzo.modo(true);
                lienzo.nuevaCueva();
                break;

            case R.id.btncamino:
                lienzo.modo(false);
                break;
            case R.id.btnguardar:
                lienzo.modo(true);
                mapa = new Mapa(lienzo.cuevaX, lienzo.cuevaY, lienzo.caminoX,
                        lienzo.caminoY, lienzo.numCuevas, lienzo.contadorCamino);
                if(mapa.Validar()) Toast.makeText(getApplicationContext(), "Mapa guardado", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "Mapa invalido.", Toast.LENGTH_LONG).show();
                break;
        }

    }

}
