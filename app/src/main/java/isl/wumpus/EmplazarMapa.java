package isl.wumpus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class EmplazarMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;  //posicion de primera cueva
    private List<Marker> marcadores =new ArrayList<Marker>();  //se almacenan todas las cuevas
    double lat = 0.0, lon = 0.0;
    private Mapa mapaWumpus;
    private String nombreMapa;
    private int idMapaReg;
    boolean puntoFijo = false;
    private Button btnPunto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emplazar_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btnPunto =(Button) findViewById(R.id.btnCoordenadas);
        btnPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fijaPunto();
            }
        });

        nombreMapa="";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                nombreMapa= null; //nunca seria null
            } else {
                idMapaReg = extras.getInt("idMR");
                if(idMapaReg==0) {//si no hay id de poliedro regular
                   nombreMapa = extras.getString("nM");
                }
            }
        } else {
            nombreMapa= (String) savedInstanceState.getSerializable("nM");
        }
        //no sirven ninguno de los constructores mapaWumpus =new Mapa(mapaWumpus.getCuevaX(),mapaWumpus.getCuevaY(),mapaWumpus.getCaminoV1(),mapaWumpus.getCaminoV2(),mapaWumpus.getContCuevas(),mapaWumpus.getContCaminos());
        GestionadorDeArchivos ga = new GestionadorDeArchivos();
        String s =ga.read(nombreMapa,getApplicationContext());
        mapaWumpus= ga.convertirStringAObjeto(s);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbic();


        mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });
    }


    //por ahora fija el punto y agrega una cantidad de cuevas , agrega varias en el mismo punto , ver etiquetas(pasa algo raro)
    private void fijaPunto(){
        if(marker.isVisible()) { // se podria hacer una mejor validacion  R: si....
            puntoFijo = true;
            marker.setDraggable(false);
        }
        int[] cuevaX= mapaWumpus.getCuevaX();
        int[] cuevaY= mapaWumpus.getCuevaY();
        int[] caminoV1= mapaWumpus.getCaminoV1();
        int[] caminoV2 = mapaWumpus.getCaminoV2();
        for(int o=1; o<cuevaX.length; o++){
            cuevaX[o]-= cuevaX[0];
            cuevaY[o]-= cuevaX[0];
        }

        //colocar los demas marcadores
       marcadores.add(marker); //marker es la primera cueva
        if(idMapaReg!=0){
            //asignarMapaReg();
        }
        int cantidadCuevas=mapaWumpus.getContCuevas(); //por ahora fijo porque mapawumpus no sirve
        for(int i =1;i<cantidadCuevas;i++) {
            double lat = marcadores.get(0).getPosition().latitude;
            double lon = marcadores.get(0).getPosition().longitude;

            // degree in google map is equal to 111.32 Kilometer. 1Degree = 111.32KM. 1KM in Degree = 1 / 111.32 = 0.008983. 1M in Degree = 0.000008983
            // agregar nuevo marcador a 5 metros markers[i]
            double metros = cuevaX[i];
            double coef = metros * 0.0000005;
            double new_lat = lat + coef;
            metros = cuevaY[i];
            coef = metros * 0.0000003;
            double new_long = lon + coef / Math.cos(lat * 0.018);
            LatLng coord = new LatLng(new_lat, new_long);
            Marker m = mMap.addMarker(new MarkerOptions().position(coord).title("x")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva)).draggable(false));

            marcadores.add(m);
            agregarOtroMarcador(new_lat, new_long, m, ""+(i+2)+""); //empieza poniendo de titulo cueva 2
        }


    }

/*    private void asignarMapaReg(){
        Mapa mapaR = new Mapa();//este mapa tiene atributos que son cada uno de los mapas regulares
        switch (idMapaReg){
            case 1:
                mapaWumpus=mapaR.tetrahedro;
               break;
            case 2:
                mapaWumpus=mapaR.octahedro;
                break;
            case 3:
                mapaWumpus= mapaR.cubo;
                break;
            case 4:
                mapaWumpus =mapaR.icosahedro;
                break;
            case 5:
                mapaWumpus =mapaR.dodecahedro;
                break;
        }
    }*/

    private void agregarMarcador(double la, double lo) {
        LatLng coord = new LatLng(la, lo);
        CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 20f);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(coord).title("Primera cueva")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva)).draggable(true));

        mMap.animateCamera(miUbic);
    }


   private void agregarOtroMarcador(double la, double lo,Marker m,String titulo) {
        LatLng coord = new LatLng(la, lo);
        CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 20f);
        m = mMap.addMarker(new MarkerOptions().position(coord).title(titulo)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva)).draggable(true));
        mMap.animateCamera(miUbic);
    }


    private void actualizarUbic(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            agregarMarcador(lat, lon);
        }
    }

    //Esto es una variable para el metodo de abajo


    private void miUbic() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);;
        try {

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    LocationListener locationListener1 = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {
                        }

                        @Override
                        public void onProviderEnabled(String s) {
                        }

                        @Override
                        public void onProviderDisabled(String s) {
                        }
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener1);

                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        LocationListener locationListener2 = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                            }
                        };
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener2);

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            actualizarUbic(location);
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
        }

    }


}
