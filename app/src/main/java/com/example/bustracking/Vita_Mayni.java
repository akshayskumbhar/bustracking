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

public class Vita_Mayni extends AppCompatActivity implements OnMapReadyCallback{

    // Define an array of LatLng objects to hold multiple locations
    private LatLng[] locations = new LatLng[]{
            // AITRC Bus Stop
            new LatLng(17.222968, 74.531285), // Bus Stop in AITRC (existing)
            // Vita
            new LatLng(17.272653, 74.538315),
            // Bhambarde
            new LatLng(17.304479, 74.572957),
            // Devnagar Fata
            new LatLng(17.313019, 74.587885),
            // Jondhalkhindi Fata
            new LatLng(17.321442, 74.611664),
            // Pentrol Pump
            new LatLng(17.327477, 74.629113),
            // Lengre School
            new LatLng(17.330328, 74.635763),
            // Lengre
            new LatLng(17.331596, 74.639942),
            // Madhalmuthi
            new LatLng(17.349082, 74.656404),
            // Devikhindi (Admin)
            new LatLng(17.381547, 74.642904),
            // Devikhindi Kaman
            new LatLng(17.382161, 74.641148),
            // Kaledhon Fata
            new LatLng(17.379439, 74.594141),
            // Bhikavadi Fata
            new LatLng(17.378040, 74.590557),
            // Vejegaon
            new LatLng(17.376361, 74.589279),
            // Shivshakti Dhaba
            new LatLng(17.431909, 74.647125),
            // Stand Fata
            new LatLng(17.432867, 74.642666),
            // Atkarimala
            new LatLng(17.424685, 74.626397),
            // High School
            new LatLng(17.436530, 74.640339),
            // Vikhale Fata
            new LatLng(17.452594, 74.626323),
            // Aatil Vikhale Fata
            new LatLng(17.458023, 74.620644),
            // Main Chowk
            new LatLng(17.443206, 74.545887),
            // Mayni
            new LatLng(17.437811, 74.546581),
    };

    private static final String[] markerTitles = {
            "AITRC Bus Stop",
            "Vita",
            "Bhambarde",
            "Devnagar Fata",
            "Jondhalkhindi Fata",
            "Pentrol Pump",
            "Lengre School",
            "Lengre",
            "Madhalmuthi",
            "Devikhindi (Admin)",
            "Devikhindi Kaman",
            "Kaledhon Fata",
            "Bhikavadi Fata",
            "Vejegaon",
            "Shivshakti Dhaba",
            "Stand Fata",
            "Atkarimala",
            "High School",
            "Vikhale Fata",
            "Aatil Vikhale Fata",
            "Main Chowk",
            "Mayni"

    };

    private TextView timeTextView;
    final Handler handler = new Handler();
    final int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vita_mayni);

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
