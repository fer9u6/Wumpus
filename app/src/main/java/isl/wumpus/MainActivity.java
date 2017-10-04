package isl.wumpus;

        import android.content.Context;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContext = getApplicationContext();

    }

}
