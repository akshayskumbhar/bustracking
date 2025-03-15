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

public class Vita_Dahivadi extends AppCompatActivity implements OnMapReadyCallback {

    // Define an array of LatLng objects to hold multiple locations
    private LatLng[] locations = new LatLng[]{
            // AITRC Bus Stop
            new LatLng(17.222968, 74.531285), // Bus Stop in AITRC (existing)
            // Karve
            new LatLng(17.199959, 74.540453),
            // Mangrul
            new LatLng(17.196600, 74.560347),
            // Bamani
            new LatLng(17.184659, 74.580525),
            // Dhamni
            new LatLng(17.188902, 74.617653),
            // Hatnoli
            new LatLng(17.160696, 74.606374),
            // Hatnur
            new LatLng(17.153380, 74.633250),
            // Manjerde
            new LatLng(17.140346, 74.683336),
            // Aaravde
            new LatLng(17.103024, 74.670046),
            // Nave Dorli
            new LatLng(17.098560, 74.686420),
            // Balgavade
            new LatLng(17.119181, 74.702517),
            // Bastwade
            new LatLng(17.112277, 74.722389),
            // Khujgaon
            new LatLng(17.101824, 74.717442),
            // Waghapur
            new LatLng(17.093808, 74.704108),
            // Kaularge
            new LatLng(17.086255, 74.690161),
            // Savalaj
            new LatLng(17.111964, 74.766205),
            // Shidhewadi
            new LatLng(17.146461, 74.775590),
            // Dhahivadi
            new LatLng(17.165069, 74.803900),


    };

    private static final String[] markerTitles = {
            "AITRC Bus Stop",
            "Karve",
            "Mangrul",
            "Bamani",
            "Dhamni",
            "Hatnoli",
            "Hatnur",
            "Manjarde",
            "Aaravade",
            "Nave Dorle",
            "Balagavde",
            "Bastavade",
            "Khujgaon",
            "Vaghapur",
            "Kaulage",
            "Savalaj",
            "Siddhewadi",
            "Dhahiwadi"
    };

    private TextView timeTextView;
    final Handler handler = new Handler();
    final int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vita_dahivadi);

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