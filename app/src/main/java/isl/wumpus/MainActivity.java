package isl.wumpus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void userstory02open(View v){
        Intent i = new Intent(this , userstory02.class);
        startActivity(i);
        //Toast.makeText(this, "You selected OK", Toast.LENGTH_SHORT).show();
    }
}
