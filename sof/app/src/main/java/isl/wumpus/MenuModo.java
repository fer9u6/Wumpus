package isl.wumpus;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuModo extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_modo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.title_theme);
        mediaPlayer.start();
    }

    public void showmsg(View view){

        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Context context = getApplicationContext();
        /*Toast.makeText(context, "Boton presionado "+buttonText,
                Toast.LENGTH_LONG).show();*/

        if(buttonText.equals("INICIAR EL JUEGO")){
            Intent i = new Intent(this, EscogerLaberinto.class);
            startActivity(i);
        }
        if(buttonText.equals("Compartir Laberinto")){
            pantallamultiopen();
        }

        }
    /*public void pantallasoloopen(){
        Intent i = new Intent(this,PantallaSolo.class);
        startActivity(i);
    }*/

    public void creditopen(View view){
        Intent i = new Intent(this, Creditos.class);
        startActivity(i);

    }

    public void pantallamultiopen(){
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }



}



