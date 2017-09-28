package isl.wumpus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class DibujarLaberinto extends AppCompatActivity implements View.OnClickListener {

    public static Lienzo lienzo;
    public Button bCueva;
    public Button bCamino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dibujar_laberinto);
        bCamino = (Button) findViewById(R.id.btncamino);
        bCueva = (Button) findViewById(R.id.btncueva);
        lienzo = (Lienzo) findViewById(R.id.lienzo);

        bCamino.setOnClickListener(this);
        bCueva.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncueva:
                lienzo.setCueva();
                break;

            case R.id.btncamino:
                break;
        }

    }

}
