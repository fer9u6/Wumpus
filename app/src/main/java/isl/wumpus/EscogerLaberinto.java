package isl.wumpus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static isl.wumpus.R.styleable.View;

public class EscogerLaberinto extends AppCompatActivity implements View.OnClickListener {


    Button btnR;
    Button btnI;
    Button btnD;
    Button btnE;
    ImageView iv;
    String title;
    int idMapaRegular;
    GestionadorDeArchivos gA;
    Regulares regular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_laberinto);


        btnR = (Button) findViewById(R.id.btnRegular);
        btnR.setOnClickListener(this);
        btnI = (Button) findViewById(R.id.btnIrregular);
        btnI.setOnClickListener(this);
        btnD = (Button) findViewById(R.id.btnDibujar);
        btnD.setOnClickListener(this);
        btnE = (Button) findViewById(R.id.btnEmplazar);
        btnE.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.ivPoliedro);
        regular = new Regulares(); regular.crearTetrahedro();
        gA = new GestionadorDeArchivos();
        title= "Tetraedro";

        idMapaRegular=0;
        }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegular:
                PopupMenu popupR = new PopupMenu(EscogerLaberinto.this, btnR);
                popupR.getMenuInflater().inflate(R.menu.menu_regulares, popupR.getMenu());

                popupR.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Carge poliedro y muestre la foto.
                        if ((title= (String) item.getTitle()).equals("Tetraedro")){
                            iv.setImageResource(R.drawable.tetraedro);
                            regular.crearTetrahedro();
                            //idMapaRegular=1;

                        }
                        if ((title= (String) item.getTitle()).equals("Octaedro")){
                            iv.setImageResource(R.drawable.octaedro);
                            regular.crearOctahedro();
                            //idMapaRegular=2;
                        }
                        if ((title= (String) item.getTitle()).equals("Cubo")){
                            iv.setImageResource(R.drawable.cubo);
                            regular.crearCubo();
                            //idMapaRegular=3;
                        }
                        if ((title= (String) item.getTitle()).equals("Icosaedro")){
                            iv.setImageResource(R.drawable.icosaedro);
                            regular.crearIcosahedro();
                            //idMapaRegular=4;
                        }
                        if ((title= (String) item.getTitle()).equals("Dodecaedro")){
                            iv.setImageResource(R.drawable.dodecaedro);
                            regular.crearDodecahedro();
                            //idMapaRegular=5;
                        }
                        return true;
                    }
                });

                popupR.show();
                break;
            case R.id.btnIrregular:

                PopupMenu popupI = new PopupMenu(EscogerLaberinto.this, btnI);

                File folder = new File(getApplicationContext().getFilesDir() + File.separator + "Mapas");
                if (folder.exists()) {
                    for (File f : folder.listFiles()) {
                        if (f.isFile())
                            popupI.getMenu().add(f.getName().split("\\.")[0]);
                    }
                }

                popupI.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Carge poliedro y muestre la foto.
                        title=item.getTitle().toString();
                        String imgpath = getFilesDir() + File.separator + "Imagenes" + File.separator + title + ".png";

                        Bitmap bitmap = BitmapFactory.decodeFile(imgpath);
                        iv.setImageBitmap(bitmap);

                        return true;
                    }
                });

                popupI.show();

                break;
            case R.id.btnDibujar:
                Intent i = new Intent(this, DibujarLaberinto.class);
                startActivity(i);
                break;
            case R.id.btnEmplazar:
                if(!gA.existe(title)) gA.write(title, gA.convertirObjetoAString(regular.retornaMapa()), this);
                Intent a = new Intent(this, EmplazarMapa.class);
                a.putExtra("nM", title);
                //a.putExtra("idMR",idMapaRegular);
                a.putExtra("idMR",0); // :P
                startActivity(a);
        }
    }


}


