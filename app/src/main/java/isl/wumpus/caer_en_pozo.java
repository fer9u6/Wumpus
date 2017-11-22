package isl.wumpus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class caer_en_pozo extends AppCompatActivity {

    private  Button newPartida;
    private Button salirJuego;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caer_en_pozo);
        newPartida = (Button)findViewById(R.id.nuevaPartida);
        // newPartida .setVisibility(View.INVISIBLE);
        newPartida.postDelayed(new Runnable() {
            public void run() {
                newPartida.setVisibility(View.VISIBLE);
            }
        }, 4000);

        salirJuego= (Button)findViewById(R.id.salir);
        // salirJuego.setVisibility(View.INVISIBLE);
        salirJuego.postDelayed(new Runnable() {
            public void run() {
                salirJuego.setVisibility(View.VISIBLE);
            }
        }, 4000);


        salirJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        newPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MenuModo.class);
                startActivity(in);
            }
        });





    }
}
