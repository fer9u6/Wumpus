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
