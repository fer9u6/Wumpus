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

    public Mapa tetrahedro;
    public Mapa octahedro;
    public Mapa cubo;
    public Mapa icosahedro;
    public Mapa dodecahedro;

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

  //  public Mapa(){
       // setTetrahedro();
   // }

    public void setTetrahedro(){
        contCuevas=4;
        cuevaX[1] = 200;
        cuevaY[1] = 70 ;
        cuevaX[2] = 350;
        cuevaY[2] = 350 ;
        cuevaX[3] = 50;
        cuevaY[3] = 350 ;
        cuevaX[4] = 200;
        cuevaY[4] = 200 ;
        //se generan 6 lineas
        caminoV1[0]= 1; caminoV2[0]=2;
        caminoV1[1]= 2; caminoV2[1]=3;
        caminoV1[2]= 3; caminoV2[2]=1;// aqui se forma el triangulo
        caminoV1[3]= 1; caminoV2[3]=4;
        caminoV1[4]= 2; caminoV2[4]=4;
        caminoV1[5]= 3; caminoV2[5]=4;
        contCaminos=6;

        /*tetrahedro.caminoV2 =caminoV2;
        tetrahedro.caminoV1=caminoV2;
        tetrahedro.cuevaX=cuevaX;
        tetrahedro.cuevaY=cuevaY;
        tetrahedro.contCaminos=contCaminos;
        tetrahedro.contCuevas=contCuevas;*/
        //tetrahedro = new Mapa();
        //tetrahedro=this;

    }

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
