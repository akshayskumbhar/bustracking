package com.example.bustracking;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Date;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLocation;
    private SupportMapFragment mapFragment;

    private Marker busMarker;
    private LatLng previousLocation = null;
    private long previousTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the FusedLocationProviderClient to get the user's location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the map fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error: Map Fragment not found!", Toast.LENGTH_SHORT).show();
        }

        // Update the time every second
        TextView timeTextView;
        final Handler handler = new Handler();
        final int delay = 1000;
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

        // Adjust padding to account for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED; // Consume the insets
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set a default marker while fetching the location
        LatLng defaultLocation = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Fetching Location..."));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5));

        // Fetch and update the location from Firebase
        fetchLocationFromFirebase();

        // Get the user's location
        fetchUserLocation();

        // Add bus stops and draw routes
        addBusStopsAndRoutes();

    }
    private void addBusStopsAndRoutes() {
        LatLng[] locations = new LatLng[]{
                new LatLng(17.222968, 74.531285), // AITRC Bus Stop
                new LatLng(17.272594, 74.532558), // Nivri Naka
                new LatLng(17.287470, 74.466882), // Aambegaon
                new LatLng(17.297780, 74.435185), // Hanmantvadiye
                new LatLng(17.298677, 74.421799), // Yevlevadi
                new LatLng(17.273122, 74.403879), // Shivni
                new LatLng(17.295098, 74.393226), // Aamrapur
                new LatLng(17.266986, 74.378733), // Hingangaon
                new LatLng(17.296575, 74.362551), // Kadepur
                new LatLng(17.326731, 74.356614), // Soholi
                new LatLng(17.296261, 74.333456), //Kadegaon
        };

        String[] markerTitles = {
                "AITRC Bus Stop",
                "Nivri Naka",
                "Aambegaon",
                "Hanmantvadiye",
                "Yevlevadi",
                "Shivni",
                "Aamrapur",
                "Hingangaon Kh",
                "Kadepur",
                "Soholi",
                "Kadegaon"
        };
        // Add markers for all stops
        for (int i = 0; i < locations.length; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(locations[i])
                    .title(markerTitles[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        // Draw routes using Polyline
        drawRoute(locations);
    }
    private void drawRoute(LatLng[] locations) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.BLUE)
                .width(10)
                .geodesic(true);

        for (LatLng location : locations) {
            polylineOptions.add(location);
        }
        mMap.addPolyline(polylineOptions);
    }

    private void fetchLocationFromFirebase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("busLocation");

        // Fetch location updates from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Object latitudeObj = dataSnapshot.child("latitude").getValue();
                    Object longitudeObj = dataSnapshot.child("longitude").getValue();

                    if (latitudeObj instanceof String && longitudeObj instanceof String) {
                        try {
                            double latitude = Double.parseDouble((String) latitudeObj);
                            double longitude = Double.parseDouble((String) longitudeObj);
                            updateMap(latitude, longitude);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MapActivity.this, "Error parsing GPS data!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(MapActivity.this, "Error processing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapActivity.this, "Error fetching data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchUserLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("Your Location"));

            } else {
                Toast.makeText(this, "Unable to fetch your location!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateMarker(final Marker marker, final LatLng toPosition) {
        final Handler handler = new Handler();
        final long start = System.currentTimeMillis();
        final long duration = 1000; // 1 second animation

        final LatLng startLatLng = marker.getPosition();
        final Interpolator interpolator = new android.view.animation.LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                double lat = (toPosition.latitude - startLatLng.latitude) * t + startLatLng.latitude;
                double lng = (toPosition.longitude - startLatLng.longitude) * t + startLatLng.longitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    handler.postDelayed(this, 16); // roughly 60 fps
                }
            }
        });
    }


    private void updateMap(double busLatitude, double busLongitude) {
        LatLng busLocation = new LatLng(busLatitude, busLongitude);

        // --- SPEED CALCULATION START ---
        long currentTime = System.currentTimeMillis();

        if (previousLocation != null && previousTime != 0) {
            float[] result = new float[1];
            Location.distanceBetween(
                    previousLocation.latitude, previousLocation.longitude,
                    busLatitude, busLongitude,
                    result
            );

            float distanceInMeters = result[0];
            long timeDiffInMillis = currentTime - previousTime;
            float timeDiffInSeconds = timeDiffInMillis / 1000f;

            if (timeDiffInSeconds > 0) {
                float speedMps = distanceInMeters / timeDiffInSeconds; // meters/second
                float speedKmph = speedMps * 3.6f;

                TextView speedTextView = findViewById(R.id.speed);
                if (speedTextView != null) {
                    speedTextView.setText(String.format(Locale.getDefault(), "%.2f km/h", speedKmph));
                }
            }
        }

        // Save current as previous for next calculation
        previousLocation = new LatLng(busLatitude, busLongitude);
        previousTime = currentTime;
        // --- SPEED CALCULATION END ---

        // Clear previous markers and add new ones
        if (busMarker == null) {
            // First time: add marker
            busMarker = mMap.addMarker(new MarkerOptions()
                    .position(busLocation)
                    .title("Bus Location")
                    .icon(getBusIcon()));
        } else {
            // Animate the marker from previous to current location
            animateMarker(busMarker, busLocation);
        }


        // Add user's location to map if available
        if (userLocation != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title("Your Location"));

            // Calculate distance between user and bus
            float[] results = new float[1];
            android.location.Location.distanceBetween(
                    userLocation.latitude, userLocation.longitude,
                    busLatitude, busLongitude,
                    results);

            // Distance is in meters; convert to kilometers and update TextView

            float distanceInKm = results[0] / 1000;
            TextView distanceTextView = findViewById(R.id.distancecal);
            if (distanceTextView != null) {
                distanceTextView.setText(String.format(Locale.getDefault(), "%.2f KM", distanceInKm));
            } else {
                Log.e("MapActivity", "Distance TextView is null!");
            }
        }


        // Move camera to show both the user and bus locations
        if (userLocation != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(busLocation);
            builder.include(userLocation);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(busLocation, 15));
        }

        addBusStopsAndRoutes(); // Keep the existing route intact
    }

    private BitmapDescriptor getBusIcon() {
        int height = 100; // Desired height in pixels
        int width = 150;  // Desired width in pixels
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.imagebus, null);
        Bitmap originalBitmap = bitmapDrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }

}