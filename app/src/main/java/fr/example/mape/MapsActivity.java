package fr.example.mape;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private LatLng latLngMarker;
    private Bitmap picture;

    public class Position implements GoogleMap.OnMapClickListener {

        @Override
        public void onMapClick(LatLng latLng) {

            Context context = getApplicationContext();
            CharSequence text = latLng.toString();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }

    public class Pin implements GoogleMap.OnMapLongClickListener {

        @Override
        public void onMapLongClick(LatLng latLng) {

            latLngMarker = latLng;

            Intent addInformationIntent = new Intent(getApplicationContext(),AddingInformation.class);
            if (addInformationIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(addInformationIntent, 1);
            }

        }
    }


    public class ShowInformation implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
            BitmapDescriptor bitmap;
            Intent  showInformation = new Intent(getApplicationContext(),Information.class);


            showInformation.putExtra("longitude",marker.getPosition().longitude);
            showInformation.putExtra("latitude",marker.getPosition().latitude);
            showInformation.putExtra("title", marker.getTitle());
            showInformation.putExtra("snippet", marker.getSnippet());

            if (showInformation.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(showInformation, 2);
            }
            return true;
        }

    }


// Creating a new complete pin
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==1){
            if (resultCode == Activity.RESULT_OK) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLngMarker);

                String title = data.getExtras().getString("information1_edit");
                markerOptions.title(title);
                System.out.println(title);

                String snippet = data.getExtras().getString("information2_edit");
                markerOptions.snippet(snippet);
                System.out.println(snippet);

               if (((Bundle)data.getExtras().getParcelable("bundle")).get("picture") != null ){
                    Bundle bundle = data.getExtras().getParcelable("bundle");
                    picture = (Bitmap)bundle.get("picture");
                    markerOptions.icon(fromBitmap(picture));
               }

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMarker));
                markers.add(markerOptions);

            }

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Persistence
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey("markers")) {
                markers = savedInstanceState.getParcelableArrayList("markers");
            }
        }

    }

    //Menu creation

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        // Persistence
        if (markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                mMap.addMarker(markers.get(i));
            }
        }


        //Permissions to manipulate the map

        if (ContextCompat.checkSelfPermission (this.getApplicationContext (), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        Position position = new Position();
        Pin marker = new Pin();
        ShowInformation choiceAction = new ShowInformation();
        googleMap.setOnMapClickListener((GoogleMap.OnMapClickListener) position);
        googleMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener)marker);
        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener)choiceAction);





        // Add a marker in Plouzané and move the camera
        LatLng plouzane = new LatLng(48.359285, -4.569933);
        mMap.addMarker(new MarkerOptions().position(plouzane).title("Plouzané"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(plouzane));

    }

    //Persistence

    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("markers",markers);
        super.onSaveInstanceState(outState);
    }

    //Menu item actions

    public void setMapTypeNormal(MenuItem item){
        mMap.setMapType(MAP_TYPE_NORMAL);
    }

    public void setMapTypeHybrid(MenuItem item){
        mMap.setMapType(MAP_TYPE_HYBRID);
    }

    public void setMapTypeSatellite(MenuItem item){
        mMap.setMapType(MAP_TYPE_SATELLITE);
    }

}




