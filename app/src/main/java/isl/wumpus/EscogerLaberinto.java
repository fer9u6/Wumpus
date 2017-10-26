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
import java.util.Stack;

import static isl.wumpus.R.styleable.View;

public class EscogerLaberinto extends AppCompatActivity implements View.OnClickListener {


    Button btnR;
    Button btnI;
    Button btnD;
    Button btnE;
    ImageView iv;
    String title;

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


        }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegular:
                PopupMenu popupR = new PopupMenu(EscogerLaberinto.this, btnR);
                popupR.getMenuInflater().inflate(R.menu.menu_regulares, popupR.getMenu());


                popupR.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Carge poliedro y muestre la foto. Comprueba si no existe primero.

                        File folder = new File(getApplicationContext().getFilesDir() + File.separator + "Mapas");

                        if (item.getTitle().equals("Tetraedro")){
                            iv.setImageResource(R.drawable.tetraedro);
                            File archivo = new File(folder + File.separator + "Tetraedro.mapa");
                            if (!archivo.exists()) {
                                guardarRegular("Tetraedro");
                            }

                        }
                        if (item.getTitle().equals("Octaedro")){
                            iv.setImageResource(R.drawable.octaedro);
                            File archivo = new File(folder + File.separator + "Octaedro.mapa");
                            if (!archivo.exists()) {
                                guardarRegular("Octaedro");
                            }
                        }
                        if (item.getTitle().equals("Cubo")){
                            iv.setImageResource(R.drawable.cubo);
                            File archivo = new File(folder + File.separator + "Cubo.mapa");
                            if (!archivo.exists()) {
                                guardarRegular("Cubo");
                            }
                        }
                        if (item.getTitle().equals("Icosaedro")){
                            iv.setImageResource(R.drawable.icosaedro);
                            File archivo = new File(folder + File.separator + "Icosaedro.mapa");
                            if (!archivo.exists()) {
                                guardarRegular("Icosaedro");
                            }
                        }
                        if (item.getTitle().equals("Dodecaedro")){
                            iv.setImageResource(R.drawable.dodecaedro);
                            File archivo = new File(folder + File.separator + "Dodecaedro.mapa");
                            if (!archivo.exists()) {
                                guardarRegular("Dodecaedro");
                            }
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
                        //No agrega los regulares al menu
                        if (f.isFile() && !f.getName().equals("Tetraedro.mapa") && !f.getName().equals("Octaedro.mapa")
                                && !f.getName().equals("Cubo.mapa") && !f.getName().equals("Icosaedro.mapa")
                                && !f.getName().equals("Dodecaedro.mapa"))
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
                Intent a = new Intent(this, EmplazarMapa.class);
                a.putExtra("nM", title);
                startActivity(a);
        }
    }
    /*
    Guarda el poliedro regular en memoria interna
     */
    private void guardarRegular (String nombre) {

        int[] cuevaY=new int[100];
        int[] cuevaX =new int[100];
        int[] caminoA=new int[100];
        int[] caminoB =new int[100];
        int contadorCamino =0;
        int numCuevas;
        GestionadorDeArchivos ga = new GestionadorDeArchivos();

        switch(nombre){
            case "Tetraedro":
                numCuevas=4;
                cuevaX[1] = 200;
                cuevaY[1] = 70 ;
                cuevaX[2] = 350;
                cuevaY[2] = 350 ;
                cuevaX[3] = 50;
                cuevaY[3] = 350 ;
                cuevaX[4] = 200;
                cuevaY[4] = 200 ;
                //se generan 6 lineas
                caminoA[0]= 1; caminoB[0]=2;
                caminoA[1]= 2; caminoB[1]=3;
                caminoA[2]= 3; caminoB[2]=1;// aqui se forma el triangulo
                caminoA[3]= 1; caminoB[3]=4;
                caminoA[4]= 2; caminoB[4]=4;
                caminoA[5]= 3; caminoB[5]=4;
                contadorCamino=6;

                Mapa tetraedro = new Mapa(cuevaX,cuevaY,caminoA,caminoB,numCuevas,contadorCamino );
                ga.write("Tetraedro",ga.convertirObjetoAString(tetraedro),getApplicationContext());
                break;
            case "Octaedro":
                numCuevas=6;
                cuevaX[1]=200;  cuevaY[1]=50;
                cuevaX[2]=380;  cuevaY[2]=370;
                cuevaX[3]=30;  cuevaY[3]=370;
                cuevaX[4]=200;  cuevaY[4]=280;
                cuevaX[5]=150;  cuevaY[5]=200;
                cuevaX[6]=240;  cuevaY[6]=200;
                //12 lineas
                caminoA[0]= 1; caminoB[0]=2;
                caminoA[1]= 2; caminoB[1]=3;
                caminoA[2]= 3; caminoB[2]=1;// aqui se forma el triangulo externo
                caminoA[3]= 5; caminoB[3]=6;
                caminoA[4]= 6; caminoB[4]=4;
                caminoA[5]= 4; caminoB[5]=5; //aqui triangulo interno
                caminoA[6]= 1; caminoB[6]=6;
                caminoA[7]= 1; caminoB[7]=5;
                caminoA[8]= 2; caminoB[8]=6;
                caminoA[9]= 2; caminoB[9]=4;
                caminoA[10]= 3; caminoB[10]=4;
                caminoA[11]= 3; caminoB[11]=5;
                contadorCamino=12;

                Mapa octaedro = new Mapa(cuevaX,cuevaY,caminoA,caminoB,numCuevas,contadorCamino );
                ga.write("Octaedro",ga.convertirObjetoAString(octaedro),getApplicationContext());
                break;
            case "Cubo":
                numCuevas=8;
                cuevaX[1]=50;   cuevaY[1]=50;
                cuevaX[2]=350;  cuevaY[2]=50;
                cuevaX[3]=350;  cuevaY[3]=350;
                cuevaX[4]=50;  cuevaY[4]=350;
                cuevaX[5]=150;  cuevaY[5]=150;
                cuevaX[6]=250;  cuevaY[6]=150;
                cuevaX[7]=250;  cuevaY[7]=250;
                cuevaX[8]=150;  cuevaY[8]=250;
                //12 lineas
                caminoA[0]= 1; caminoB[0]=2;
                caminoA[1]= 2; caminoB[1]=3;
                caminoA[2]= 3; caminoB[2]=4;
                caminoA[3]= 4; caminoB[3]=1;//cuadrado externo
                caminoA[4]= 5; caminoB[4]=6;
                caminoA[5]= 6; caminoB[5]=7;
                caminoA[6]= 7; caminoB[6]=8;
                caminoA[7]= 8; caminoB[7]=5;//cuadrado interno
                caminoA[8]= 1; caminoB[8]=5;
                caminoA[9]= 2; caminoB[9]=6;
                caminoA[10]= 3; caminoB[10]=7;
                caminoA[11]= 4; caminoB[11]=8;
                contadorCamino=12;

                Mapa cubo = new Mapa(cuevaX,cuevaY,caminoA,caminoB,numCuevas,contadorCamino );
                ga.write("Cubo",ga.convertirObjetoAString(cubo),getApplicationContext());
                break;
            case "Icosaedro":
                numCuevas=12;
                cuevaX[1]=200;  cuevaY[1]=10;
                cuevaX[2]=400;  cuevaY[2]=400;
                cuevaX[3]=10;  cuevaY[3]=400;
                cuevaX[4]=200;  cuevaY[4]=90;
                cuevaX[5]=110;  cuevaY[5]=180;
                cuevaX[6]=170;  cuevaY[6]=200;
                cuevaX[7]=230;  cuevaY[7]=200;
                cuevaX[8]=290;  cuevaY[8]=180;
                cuevaX[9]=290;  cuevaY[9]=300;
                cuevaX[10]=200;  cuevaY[10]=360;
                cuevaX[11]=100;  cuevaY[11]=300;
                cuevaX[12]=200;  cuevaY[12]=280;
                //30 lineas
                caminoA[0]= 1; caminoB[0]=2;
                caminoA[1]= 2; caminoB[1]=3;
                caminoA[2]= 3; caminoB[2]=1;// aqui se forma el triangulo externo
                caminoA[3]= 1; caminoB[3]=5;
                caminoA[4]= 1; caminoB[4]=4;
                caminoA[5]= 1; caminoB[5]=8;
                caminoA[6]= 2; caminoB[6]=8;
                caminoA[7]= 2; caminoB[7]=9;
                caminoA[8]= 2; caminoB[8]=10;
                caminoA[9]= 3; caminoB[9]=5;
                caminoA[10]= 3; caminoB[10]=11;
                caminoA[11]= 3; caminoB[11]=10;
                caminoA[12]= 4; caminoB[12]=5;
                caminoA[13]= 4; caminoB[13]=6;
                caminoA[14]= 4; caminoB[14]=7;
                caminoA[15]= 4; caminoB[15]=8;
                caminoA[16]= 8; caminoB[16]=9;
                caminoA[17]= 9; caminoB[17]=10;
                caminoA[18]= 10; caminoB[18]=11;
                caminoA[19]= 11; caminoB[19]=5;
                caminoA[20]= 5; caminoB[20]=6;
                caminoA[21]= 6; caminoB[21]=7;
                caminoA[22]= 7; caminoB[22]=8;
                caminoA[23]= 12; caminoB[23]=10;
                caminoA[24]= 12; caminoB[24]=11;
                caminoA[25]= 6; caminoB[25]=11;
                caminoA[26]= 12; caminoB[26]=6;
                caminoA[27]= 12; caminoB[27]=7;
                caminoA[28]= 12; caminoB[28]=9;
                caminoA[29]= 7; caminoB[29]=9;
                contadorCamino=30;

                Mapa icosaedro = new Mapa(cuevaX,cuevaY,caminoA,caminoB,numCuevas,contadorCamino );
                ga.write("Icosaedro",ga.convertirObjetoAString(icosaedro),getApplicationContext());
                break;
            case "Dodecaedro":
                numCuevas=20;
                cuevaX[1]=200;   cuevaY[1]=10;
                cuevaX[2]=400;  cuevaY[2]=160;
                cuevaX[3]=330;  cuevaY[3]=400;
                cuevaX[4]=80;  cuevaY[4]=400;
                cuevaX[5]=10;  cuevaY[5]=160;
                cuevaX[6]=200;  cuevaY[6]=80;
                cuevaX[7]=340;  cuevaY[7]=190;
                cuevaX[8]=290;  cuevaY[8]=330;
                cuevaX[9]=110;  cuevaY[9]=330;
                cuevaX[10]=70;  cuevaY[10]=190;
                cuevaX[11]=200;  cuevaY[11]=360;
                cuevaX[12]=100;  cuevaY[12]=270;
                cuevaX[13]=110;  cuevaY[13]=140;
                cuevaX[14]=290;  cuevaY[14]=140;
                cuevaX[15]=300;  cuevaY[15]=270;
                cuevaX[16]=200;  cuevaY[16]=300;
                cuevaX[17]=160;  cuevaY[17]=250;
                cuevaX[18]=180;  cuevaY[18]=160;
                cuevaX[19]=240;  cuevaY[19]=160;
                cuevaX[20]=250;  cuevaY[20]=250;

                //30 lineas
                caminoA[0]= 1; caminoB[0]=2;
                caminoA[1]= 2; caminoB[1]=3;
                caminoA[2]= 3; caminoB[2]=4;
                caminoA[3]= 4; caminoB[3]=5;
                caminoA[4]= 5; caminoB[4]=1; //pentagono
                caminoA[5]= 1; caminoB[5]=6;
                caminoA[6]= 2; caminoB[6]=7;
                caminoA[7]= 3; caminoB[7]=8;
                caminoA[8]= 4; caminoB[8]=9;
                caminoA[9]= 5; caminoB[9]=10;//pentagono a vertice mas cercano
                caminoA[10]= 13; caminoB[10]=6;
                caminoA[11]= 13; caminoB[11]=10;
                caminoA[12]= 14; caminoB[12]=7;
                caminoA[13]= 14; caminoB[13]=6;
                caminoA[14]= 15; caminoB[14]=7;
                caminoA[15]= 15; caminoB[15]=8;
                caminoA[16]= 11; caminoB[16]=9;
                caminoA[17]= 11; caminoB[17]=8;
                caminoA[18]= 12; caminoB[18]=9;
                caminoA[19]= 12; caminoB[19]=10;
                caminoA[20]= 16; caminoB[20]=17;
                caminoA[21]= 17; caminoB[21]=18;
                caminoA[22]= 18; caminoB[22]=19;
                caminoA[23]= 19; caminoB[23]=20;
                caminoA[24]= 20; caminoB[24]=16;
                caminoA[25]= 16; caminoB[25]=11;
                caminoA[26]= 17; caminoB[26]=12;
                caminoA[27]= 18; caminoB[27]=13;
                caminoA[28]= 19; caminoB[28]= 14;
                caminoA[29]= 20; caminoB[29]=15;
                contadorCamino=30;

                Mapa dodecaedro = new Mapa(cuevaX,cuevaY,caminoA,caminoB,numCuevas,contadorCamino );
                ga.write("Dodecaedro",ga.convertirObjetoAString(dodecaedro),getApplicationContext());
                break;
        }


    }


}


