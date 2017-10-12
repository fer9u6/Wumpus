package isl.wumpus;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


/**
 * Created by b41441 on 28/09/2017.
 */

public class Lienzo extends View {
    private Path drawPath;
    private static Paint drawPaint;
    private static Paint canvasPaint;
    private static int paintColor = 0xFFFF0000;
    private static boolean borrado=false;

    private int mPivotX = 0;
    private int mPivotY = 0;
    private int radius = 30;
    private Canvas drawCanvas;
    //arrays que contienen las posiciones de las cuevas
    public int[] cuevaX;
    public int[] cuevaY;
    public int []caminoA;
    public int []caminoB;
    public int cuevasCamino[];//cuevas que se unen por la linea

    public Lienzo tretrahedro;


    //private Bitmap cuevas[];
    public int numCuevas =0 ; //contador de cuevas
    private int cueva=-1; //identificador de cueva
    private boolean modocueva;
    public int contadorCamino;
    private int contadorToques;
    private Stack<Boolean> ultimo;

    public Lienzo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
        cuevaY=new int[100];
        cuevaX =new int[100];
        caminoA=new int[100];
        caminoB =new int[100];
        contadorCamino =0;
        contadorToques=0;
        cuevasCamino= new int [2];
        ultimo = new Stack<Boolean>();

    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        canvasPaint = new Paint();
        drawCanvas =new Canvas();
    }

   //Pinta la vista. Será llamado desde el OnTouchEvent
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(drawPath, drawPaint);

            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.cueva);
            for (int i = 1; i <= numCuevas; i++) {
                canvas.drawBitmap(b, cuevaX[i], cuevaY[i], canvasPaint);
            }
           for(int i=0;i<contadorCamino;i++){
               int mitadCueva = 24;//cueva 48x48
               canvas.drawLine(cuevaX[caminoA[i]]+mitadCueva,cuevaY[caminoA[i]]+mitadCueva,cuevaX[caminoB[i]]+mitadCueva,cuevaY[caminoB[i]]+mitadCueva,canvasPaint);
           }
    }

    public void crearTetrahedro(){
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

            invalidate();
    }

    public void crearOctahedro(){
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

        invalidate();
    }

    public void crearCubo(){
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
        invalidate();
    }

    public void crearIcosahedro(){
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

        invalidate();

    }

    public void crearDodecahedro(){
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

        invalidate();

    }


    public void borrar(){
        boolean var =false;
        if(!ultimo.empty()){
           var= ultimo.pop();
        }
        if(var == false){ //camino
            if(contadorCamino!= 0)contadorCamino --;
        }else{
            if(numCuevas != 0)numCuevas --;
        }

        invalidate();
    }


    //Registra los touch de usuario
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (modocueva == true) {
            switch (event.getAction()) {
                //min =70
                case MotionEvent.ACTION_DOWN:
                    for (int i = 1; i <= numCuevas; i++) {
                        double cenx = touchX - cuevaX[i];//x original
                        double ceny = touchY - cuevaY[i];//y original
                        //que cueva se esta tocando
                        float distance = (float) Math.sqrt(cenx * cenx + ceny * ceny);
                        if (distance <= 70) { //radio original   //compara con min
                            cueva = i;
                            //min=distance
                            invalidate();
                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (cueva != -1) {
                        cuevaX[cueva] = (int) touchX;
                        cuevaY[cueva] = (int) touchY;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    return false;
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    for (int i = 1; i <= numCuevas; i++) {
                        double cenx = touchX - cuevaX[i];//x original
                        double ceny = touchY - cuevaY[i];//y original
                        //que cuava se esta tocando
                        float distance = (float) Math.sqrt(cenx * cenx + ceny * ceny);
                        if (distance <= 70) { //radio original   //compara con min
                            cueva = i;
                        }
                    }
                    cuevasCamino[contadorToques]= cueva;
                    if(contadorToques==1){
                        crearLinea();
                    }
                    ++contadorToques;
                    contadorToques%=2;

                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:


                    break;

                default:
                    return false;

            }

        }//fin else

            invalidate();
            return true;

        }


    //originalmente era para dibujar un circulo
    public void nuevaCueva() {
        modocueva=true;
        int minX = radius * 2;
        int maxX = getWidth() - (radius *2 );
        int minY = radius * 2;
        int maxY = getHeight() - (radius *2 );
        //Generate random numbers for x and y locations of the circle on screen
        Random random = new Random();
        mPivotX = random.nextInt(maxX - minX + 1) + minX;
        mPivotY = random.nextInt(maxY - minY + 1) + minY;

        //se inserta un nueva cueva en los array de posiciones
        numCuevas++; //la posicon 0 del array no se usa
        cuevaX[numCuevas] =mPivotX;
        cuevaY[numCuevas] = mPivotY;
        ultimo.push(true);
       invalidate();

    }


    public void crearLinea(){
        //agregue esta parte que prevalida
        boolean valido= true;
        if(cuevasCamino[0] == cuevasCamino[1]) valido = false;
        else{
            for(int i=0; i <= contadorCamino; i++){
                if(caminoB[i]==cuevasCamino[0] && caminoA[i]==cuevasCamino[1]) valido = false;
            }
        }
        if(valido){
            caminoA[contadorCamino]=cuevasCamino[0];
            caminoB[contadorCamino]=cuevasCamino[1];
            contadorCamino++;
            ultimo.push(false);
            invalidate();
        }

    }
    public void modo(boolean m ){
        modocueva = m;
    }
}
