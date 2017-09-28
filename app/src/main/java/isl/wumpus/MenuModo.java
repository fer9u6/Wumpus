package isl.wumpus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.R.id.message;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MenuModo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_modo);
    }
    public void showmsg(View view){

        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Context context = getApplicationContext();
        Toast.makeText(context, "Boton presionado "+buttonText,
                Toast.LENGTH_LONG).show();

        }
    }



