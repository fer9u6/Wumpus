package isl.wumpus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static isl.wumpus.R.styleable.View;

public class EscogerLaberinto extends AppCompatActivity implements View.OnClickListener {

    Button btnR;
    Button btnD;
    Button btnE;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_laberinto);


        btnR = (Button) findViewById(R.id.btnRegular);
        btnR.setOnClickListener(this);
        btnD = (Button) findViewById(R.id.btnDibujar);
        btnD.setOnClickListener(this);
        btnE = (Button) findViewById(R.id.btnEmplazar);
        btnE.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.ivPoliedro);


        }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegular:
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(EscogerLaberinto.this, btnR);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_regulares, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Carge poliedro y muestre la foto.
                        if (item.getTitle().equals("Tetraedro")){
                            iv.setImageResource(R.drawable.tetraedro);
                        }
                        if (item.getTitle().equals("Octaedro")){
                            iv.setImageResource(R.drawable.octaedro);
                        }
                        if (item.getTitle().equals("Cubo")){
                            iv.setImageResource(R.drawable.cubo);
                        }
                        if (item.getTitle().equals("Icosaedro")){
                            iv.setImageResource(R.drawable.icosaedro);
                        }
                        if (item.getTitle().equals("Dodecaedro")){
                            iv.setImageResource(R.drawable.dodecaedro);
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.btnDibujar:
                Intent i = new Intent(this, DibujarLaberinto.class);
                startActivity(i);
                break;
            case R.id.btnEmplazar:
                Intent a = new Intent(this, EmplazarMapa.class);
                startActivity(a);
        }
    }
}


