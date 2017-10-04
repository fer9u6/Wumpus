package isl.wumpus;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void toBluetoothActivity(View v){
        Intent i = new Intent(this , Bluetooth.class);
        startActivity(i);
        //Toast.makeText(this, "You selected OK", Toast.LENGTH_SHORT).show();
    }

}
