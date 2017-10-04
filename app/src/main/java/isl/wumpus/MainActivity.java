package isl.wumpus;

        import android.content.Context;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.Toast;

        import static isl.wumpus.GestionadorDeArchivos.convertirObjetoAString;
        import static isl.wumpus.GestionadorDeArchivos.convertirStringAObjeto;

public class MainActivity extends AppCompatActivity {

    static Context appContext;
    GestionadorDeArchivos ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ga = new GestionadorDeArchivos(this);

        appContext = getApplicationContext();


        String a = "estoesunaprueba";
        Object o = convertirStringAObjeto(a);

        System.out.println(convertirObjetoAString(o));
        ga.write("prueba",convertirObjetoAString(o),this);
        Toast.makeText(this.getApplicationContext(),ga.read("prueba", this),Toast.LENGTH_LONG).show();
    }


}
