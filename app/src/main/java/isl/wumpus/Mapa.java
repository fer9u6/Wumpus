package isl.wumpus;

/**
 * Created by usuario on 03/10/2017.
 */

public class Mapa {
    private int[] cuevaX;
    private int[] cuevaY;
    private int[] caminoV1;
    private int[] caminoV2;
    private int contCuevas;
    private int contCaminos;

    public int[] getCuevaX(){
        return cuevaX;
    }
    public int[] getCuevaY(){
        return cuevaY;
    }
    public int[] getCaminoV1(){
        return caminoV1;
    }
    public int[] getCaminoV2(){
        return caminoV2;
    }
    public int getContCuevas(){ return contCuevas;}
    public int getContCaminos(){return contCaminos;}



    public Mapa(int[] cX, int[] cY, int[] cV1, int[] cV2, int cCuevas, int cCaminos){
        cuevaX = cX;
        cuevaY = cY;
        caminoV1 = cV1;
        caminoV2 = cV2;
        contCuevas = cCuevas;
        contCaminos = cCaminos;
    }

    /**
     * Metodo que verifica si el mapa es valido, que cada cueva tenga por lo menos un camino que la conecte con el mapa
     * @return si es un mapa valido o no
     */
    public boolean Validar(){
        boolean esValido = true;
        for(int j=1; j <= contCuevas; j++){
            boolean hayCamino=false;
            for(int i=0; i < contCaminos; i++){  //en vez de <=
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
