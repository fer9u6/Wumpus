package isl.wumpus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuModo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_modo);
    }
    public void showmsg(View view){

        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Context context = getApplicationContext();
        /*Toast.makeText(context, "Boton presionado "+buttonText,
                Toast.LENGTH_LONG).show();*/

        if(buttonText.equals("SOLO")){
            Intent i = new Intent(this, EscogerLaberinto.class);
            startActivity(i);
        }
        if(buttonText.equals("MULTI")){
            pantallamultiopen();
        }

        }
    public void pantallasoloopen(){
        Intent i = new Intent(this,PantallaSolo.class);
        startActivity(i);
    }

    public void pantallamultiopen(){
        Intent i = new Intent(this,PantallaMulti.class);
        startActivity(i);
    }

    }



