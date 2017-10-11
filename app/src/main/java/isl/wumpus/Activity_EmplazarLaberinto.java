package isl.wumpus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_EmplazarLaberinto extends AppCompatActivity implements View.OnClickListener  {

    public Button bCoordenadas;
    public EditText tDistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__emplazar_laberinto);
        bCoordenadas = (Button)findViewById(R.id.btnCoordenadasCueva);
        bCoordenadas.setOnClickListener(this);
        tDistancia =(EditText) findViewById(R.id.textDistancia);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCoordenadasCueva:
               // Intent i = new Intent(this,MapsActivity.class);
               // startActivity(i);
                break;
        }
    }
}
