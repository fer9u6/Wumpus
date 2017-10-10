package isl.wumpus;

/**
 * Created by usuario on 03/10/2017.
 */

public class Mapa {
    private float[] cuevaX;
    private float[] cuevaY;
    private int[] caminoV1;
    private int[] caminoV2;
    private int contCuevas;
    private int contCaminos;

    public float[] getCuevaX(){
        return cuevaX;
    }
    public float[] getCuevaY(){
        return cuevaY;
    }
    public int[] getCaminoV1(){
        return caminoV1;
    }
    public int[] getCaminoV2(){
        return caminoV2;
    }

    public Mapa(float[] cX, float[] cY, int[] cV1, int[] cV2, int cCuevas, int cCaminos){
        cuevaX = cX;
        cuevaY = cY;
        caminoV1 = cV1;
        caminoV2 = cV2;
        contCuevas = cCuevas;
        contCaminos = cCaminos;
    }

    public boolean Validar(){
        boolean esValido = true;
        for(int j=1; j <= contCuevas; j++)//El vector de cuevas empieza de la posición 1.
        {
            boolean hayCamino=false;
            for(int i=0; i < contCaminos; i++)//El vector de caminos empieza de la posición 0.
            {
                if(caminoV2[i]==j || caminoV1[i]==j) {
                    hayCamino = true; break;
                }
            }
            if(!hayCamino) {
                esValido= false; break;
            }
        }
        return esValido;
    }
}
