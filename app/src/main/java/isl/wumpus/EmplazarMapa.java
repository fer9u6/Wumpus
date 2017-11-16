package isl.wumpus;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmplazarMapa extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        ResultCallback<Status> {

    private GoogleMap mMap;
    private Marker marker;  //posicion de primera cueva
    private List<Marker> marcadores =new ArrayList<Marker>();  //se almacenan todas las cuevas
    double lat = 0.0, lon = 0.0;
    private Mapa mapaWumpus;
    private String nombreMapa;
    private int idMapaReg;
    boolean puntoFijo = false;
    private Button btnPunto;
    private Random random;
    private int [] elementosDeMapa;
    private GoogleApiClient googleApiClient;

    private Button btnRA;
    private ArrayList<LatLng> latlngArray;
    private int[] caminosA;
    private int[] caminosB;
    private ArrayList<Polyline> lineas;
    private boolean emplazado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emplazar_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        random = new Random(System.currentTimeMillis());
        btnPunto =(Button) findViewById(R.id.btnCoordenadas);
        btnRA=(Button)findViewById(R.id.btnRealidad) ;
        btnPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emplazado){fijaPunto(); emplazado=true; btnPunto.setText("Ordenar Lineas");}
                else {
                    dibujaLineas();
                }
            }
        });
        btnRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irARealidad();
            }
        });
        latlngArray = new ArrayList<>();
        nombreMapa="";
        lineas= new ArrayList<>();
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
        elementosDeMapa = new int[mapaWumpus.getContCuevas()];
        //se obtiene los caminos del mapa , con el fin de enviarlos a la actividad realidad aumentada
        caminosA=mapaWumpus.getCaminoV1();
        caminosB=mapaWumpus.getCaminoV2();
        genereElementos();
        mapFragment.getMapAsync(this);
        createGoogleApi();
    }

    private void genereElementos(){
        ArrayList<Integer> posiciones= new ArrayList<>(); int cant;
        if(elementosDeMapa.length<4){
            cant=3;
        }else cant=2;
        while (cant>0){
            Integer r= random.nextInt(elementosDeMapa.length);
            if(!posiciones.contains(r)){
                cant--; posiciones.add(r);
            }
        }
        //for(int j=0; j<elementosDeMapa.length; j++) elementosDeMapa[j]= 0;
        elementosDeMapa[(int) posiciones.remove(0)] = 2;
        elementosDeMapa[(int) posiciones.remove(0)] = 1;
        if(!posiciones.isEmpty())elementosDeMapa[(int) posiciones.remove(0)] = 1;
    }

    /**
     * Este metodo le pasa como parametro las coordenadas de las cuevas del mapa elegido a la actividad de la realidad aumentada
     * para que pueda crear los geoobject y la inicia.
     * Se invoca al presionar el boton jugar.
     */
    public void irARealidad() {

        // putExtra array coordenadas
        for(Marker ll: marcadores){
            latlngArray.add(ll.getPosition());
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("latlngArray", latlngArray);
        bundle.putIntArray("caminosA",caminosA);
        bundle.putIntArray("caminosB",caminosB);
        Intent a = new Intent(this, RealidaAumentada.class);
        a.putExtras(bundle);
        startActivity(a);
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
        miUbic();
    }


    /**
     * Hace que la primer cueva no se pueda cambiar de ubicacion en el mapa y coloca el resto de las cuevas y se agregan al array de
     * coordenadas que se le va a pasar a la realidad aumentada.
     */
    private void fijaPunto(){
        if(marker.isVisible()) { // se podria hacer una mejor validacion  R: si....
            puntoFijo = true;
            marker.setDraggable(false);
            //LatLng de primera cueva.
            //LatLng latlngActual = marker.getPosition(); //new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
            //latlngArray.add(latlngActual);
        }
        int[] cuevaX= mapaWumpus.getCuevaX();
        int[] cuevaY= mapaWumpus.getCuevaY();
        for(int o=2; o<cuevaX.length; o++){
            cuevaX[o]-= cuevaX[1];
            cuevaY[o]-= cuevaY[1];
        }

        //colocar los demas marcadores
       marcadores.add(marker); //marker es la primera cueva
        if(idMapaReg!=0){
            //asignarMapaReg();
        }
        int cantidadCuevas=mapaWumpus.getContCuevas(); //por ahora fijo porque mapawumpus no sirve
        double lat = marcadores.get(0).getPosition().latitude;
        double lon = marcadores.get(0).getPosition().longitude;
        for(int i =2;i<=cantidadCuevas;i++) {


            // degree in google map is equal to 111.32 Kilometer. 1Degree = 111.32KM. 1KM in Degree = 1 / 111.32 = 0.008983. 1M in Degree = 0.000008983
            // agregar nuevo marcador a 5 metros markers[i]
            double metros = cuevaX[i];
            double coef = metros * 0.0000007;
            double new_lat = lat + coef;
            metros = cuevaY[i];
            coef = metros * 0.0000007;
            double new_long = lon + coef /* Math.cos(lat * 0.018)*/;
            LatLng coord = new LatLng(new_lat, new_long);
            String x = ""+(i+2)+"";
            Marker m = mMap.addMarker(new MarkerOptions().position(coord).title(x)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva8bit)).draggable(true));
            marcadores.add(m);
            //agregarOtroMarcador(new_lat, new_long, m, ""+(i+2)+""); //empieza poniendo de titulo cueva 2
        }
        dibujaLineas();
        startGeofence();

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

    /**
     * Agrega un marcador que representa la ubicacion de una cueva
     * @param la latitud de la cueva que se va a agregar
     * @param lo longitud de la cueva que se va a agregar
     */
    private void agregarMarcador(double la, double lo ) {
        LatLng coord = new LatLng(la, lo);
        CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 20f);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(coord).title("Primera cueva")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva8bit)).draggable(true));

        mMap.animateCamera(miUbic);
    }


   private void agregarOtroMarcador(double la, double lo,Marker m,String titulo) {
        LatLng coord = new LatLng(la, lo);
        CameraUpdate miUbic = CameraUpdateFactory.newLatLngZoom(coord, 20f);
        m = mMap.addMarker(new MarkerOptions().position(coord).title(titulo)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.cueva8bit)).draggable(true));
        mMap.animateCamera(miUbic);
    }

    private void dibujaLineas(){
        for(Polyline p: lineas){
            p.remove();
        }
        int v1[]=mapaWumpus.getCaminoV1();
        int v2[]=mapaWumpus.getCaminoV2();
        for(int i=0; i< v1.length; i++){
            if(v1[i]!= 0 || v2[i]!=0) lineas.add(drawLine(marcadores.get(v1[i]-1).getPosition(),marcadores.get(v2[i]-1).getPosition()));
        }
    }

    @NonNull
    private Polyline drawLine(LatLng x, LatLng y){
        return  mMap.addPolyline(new PolylineOptions()
                .add(x,y)
                .width(5)
                .color(Color.RED));
    }


    private void actualizarUbic(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            agregarMarcador(lat, lon);
        }
    }

    //Esto es una variable para el metodo de abajo

    /**
     * Metodo que consigue la ubicacion actual de el usuario mediante varios parametros
     */
    private void miUbic() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            askPermission();
            return;
        }

        LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);;
        Location location= null;
        try {

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                int i;
                // location service disabled
            } else {
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled && !isNetworkEnabled ) {
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
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener1, null);

                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        while (location==null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
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
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener2, null);

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            while (location==null){
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
        }
        actualizarUbic(location);
    }

    private static final String TAG = EmplazarMapa.class.getSimpleName();
    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        int sizelatlng=latlngArray.size();
        Geofence geofence;
        GeofencingRequest geofenceRequest;
        for(int i =0;i<sizelatlng-1;i++){
            geofence = createGeofence( latlngArray.get(i), GEOFENCE_RADIUS );
            geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        }


    }


    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 7.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence( LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    LocationServices locationServices;

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            locationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            //saveGeofence();
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();
        int sizelatlng=latlngArray.size();
        for(int i =0;i<sizelatlng-1;i++){
            CircleOptions circleOptions = new CircleOptions()
                    .center( latlngArray.get(i))
                    .strokeColor(Color.argb(50, 70,70,70))
                    .fillColor( Color.argb(100, 150,150,150) )
                    .radius( GEOFENCE_RADIUS );
            geoFenceLimits = mMap.addCircle( circleOptions );
        }
    }

    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";

    // Saving GeoFence marker with prefs mng
    private void saveGeofence() {
        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong( KEY_GEOFENCE_LAT, Double.doubleToRawLongBits( latlngArray.get(0).latitude ));
        editor.putLong( KEY_GEOFENCE_LON, Double.doubleToRawLongBits( latlngArray.get(0).longitude ));
        editor.apply();
    }
    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }



    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    //askPermission();

                } else {
                    // Permission denied
                    askPermission();
                }
                break;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");

    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }
}