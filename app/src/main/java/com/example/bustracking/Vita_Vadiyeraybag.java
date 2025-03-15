package com.example.bustracking;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Vita_Vadiyeraybag extends AppCompatActivity implements OnMapReadyCallback{

    // Define an array of LatLng objects to hold multiple locations
    private LatLng[] locations = new LatLng[]{
            // AITRC Bus Stop
            new LatLng(17.222968, 74.531285), // Bus Stop in AITRC (existing)
            // Vita Bypass
            new LatLng(17.265088, 74.535750),
            // Surya Nagar
            new LatLng(17.261709, 74.514548),
            // Shirgaon Fata
            new LatLng(17.197896, 74.418720),
            // Shirgaon
            new LatLng(17.193899, 74.415065),
            // Deorashtra
            new LatLng(17.165420, 74.389502),
            // Mohite Vadgaon
            new LatLng(17.179232, 74.371642),
            // Asad Fata
            new LatLng(17.183963, 74.350740),
            // Ambak Chichni
            new LatLng(17.203875, 74.348460),
            // Ambak Fata
            new LatLng(17.221074, 74.364274),
            // Vangi
            new LatLng(17.232052, 74.398735),
            // Shelakbav
            new LatLng(17.246751, 74.448539),
            // Vadiyeraybag
            new LatLng(17.281013, 74.435990),

    };

    private static final String[] markerTitles = {
            "AITRC Bus Stop",
            "Vita Bypass",
            "Surya Nagar",
            "Shirgaon Fata",
            "Shirgaon",
            "Deorashtra",
            "Mohite Vadgaon",
            "Asad Fata",
            "Ambak Chichni",
            "Ambak Fata",
            "Vangi",
            "Shelakbav",
            "Vadiyeraybag"
    };

    private TextView timeTextView;
    final Handler handler = new Handler();
    final int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vita_vadiyeraybag);

        timeTextView = findViewById(R.id.timeTextView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                timeTextView.setText(currentTime);

                handler.postDelayed(this, delay);
            }
        }, delay);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Consume the insets
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
// Add markers for each location with unique titles
        for (int i = 0; i < locations.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(locations[i])
                    .title(markerTitles[i])); // Use unique titles from the array
        }
        // Move camera to the first location (adjust as needed)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(locations[0]));
        googleMap.setMinZoomPreference(12f); // Set minimum zoom level
        googleMap.setMaxZoomPreference(18f); // Set maximum zoom level

        // Draw the route line on the map
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(Arrays.asList(locations)) // Add all locations to the polyline
                .color(Color.BLUE) // Set the color of the line
                .width(10); // Set the width of the line
        googleMap.addPolyline(polylineOptions);
    }

}