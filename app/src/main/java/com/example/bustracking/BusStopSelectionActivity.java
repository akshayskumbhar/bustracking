package com.example.bustracking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class BusStopSelectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BusStopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_selection);

        recyclerView = findViewById(R.id.bus_stop_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BusStop> busStops = getBusStops();
        adapter = new BusStopAdapter(busStops, this::onStopSelected);
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Not needed
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }private List<BusStop> getBusStops() {
        List<BusStop> busStops = new ArrayList<>();
        busStops.add(new BusStop("AITRC Bus Stop", 17.222968, 74.531285));
        busStops.add(new BusStop("Vita", 17.286316, 74.536006));
        busStops.add(new BusStop("Nivri naka", 17.200123, 74.521765));
        busStops.add(new BusStop("Aambegaon", 17.189456, 74.515678));
        busStops.add(new BusStop("Hingangaon Kh", 17.174567, 74.501234));
        busStops.add(new BusStop("Hanmantvadiye", 17.222968, 74.531285));
        busStops.add(new BusStop("Yevlevadi", 17.215678, 74.529134));
        busStops.add(new BusStop("Shivani", 17.200123, 74.521765));
        busStops.add(new BusStop("Aamrapur", 17.189456, 74.515678));
        busStops.add(new BusStop("Kadepur", 17.174567, 74.501234));
        busStops.add(new BusStop("Soholi", 17.222968, 74.531285));
        busStops.add(new BusStop("Devikhindi (Admin)", 17.215678, 74.529134));
        busStops.add(new BusStop("Mayni", 17.200123, 74.521765));
        busStops.add(new BusStop("Nivri", 17.189456, 74.515678));
        busStops.add(new BusStop("Khanapur", 17.174567, 74.501234));
        busStops.add(new BusStop("Tasgaon", 17.222968, 74.531285));
        busStops.add(new BusStop("Bhambarde", 17.215678, 74.529134));
        busStops.add(new BusStop("Lengre", 17.200123, 74.521765));
        busStops.add(new BusStop("vejegaon", 17.189456, 74.515678));
        busStops.add(new BusStop("Salshinge", 17.174567, 74.501234));
        busStops.add(new BusStop("Kaledhon", 17.222968, 74.531285));
        busStops.add(new BusStop("Kalambi", 17.215678, 74.529134));
        busStops.add(new BusStop("Bhalavani", 17.200123, 74.521765));
        busStops.add(new BusStop("Kundal", 17.189456, 74.515678));
        busStops.add(new BusStop("Kurli", 17.174567, 74.501234));
        return busStops;
    }

    private void onStopSelected(BusStop stop) {
        Toast.makeText(this, "Selected: " + stop.getStopName(), Toast.LENGTH_SHORT).show();
        // Save the selected stop (same logic as before)
    }
}