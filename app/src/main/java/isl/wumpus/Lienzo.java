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
    public float[] cuevaX;
    public float[] cuevaY;
    public int[]caminoX;
    public int[]caminoY;
    public int cuevasCamino[];//cuevas que se unen por la linea

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
        cuevaY=new float[100];
        cuevaX =new float[100];
        caminoX=new int[100];
        caminoY =new int[100];
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
               canvas.drawLine(cuevaX[caminoX[i]]+30f,cuevaY[caminoX[i]]+30f,cuevaX[caminoY[i]]+30f,cuevaY[caminoY[i]]+30f,canvasPaint);
           }
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
                if(caminoY[i]==cuevasCamino[0] && caminoX[i]==cuevasCamino[1]) valido = false;
            }
        }
        if(valido){
            caminoX[contadorCamino]=cuevasCamino[0];
            caminoY[contadorCamino]=cuevasCamino[1];
            contadorCamino++;
            ultimo.push(false);
            invalidate();
        }

    }
    public void modo(boolean m ){
        modocueva = m;
    }
}
