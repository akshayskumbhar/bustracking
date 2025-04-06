package com.example.bustracking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {

    private EditText fromLocation, toLocation;
    private Button departNowButton;
    private RecyclerView transportRecyclerView;
    private TransportAdapter transportAdapter;
    private List<TransportOption> transportOptions;

    public class TransportOverviewFragment extends Fragment {

        // Default constructor required for fragments
        public TransportOverviewFragment() {
            super(); // Explicit call to Fragment's constructor (optional)
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // Initialize UI elements
        fromLocation = view.findViewById(R.id.etFrom);
        toLocation = view.findViewById(R.id.etTo);
        departNowButton = view.findViewById(R.id.btnDepart);
        transportRecyclerView = view.findViewById(R.id.recyclerView);

        // Initialize RecyclerView
        transportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transportOptions = getDummyTransportOptions(); // Fetch transport options
        transportAdapter = new TransportAdapter(transportOptions);
        transportRecyclerView.setAdapter(transportAdapter);

        departNowButton.setOnClickListener(v -> {
            // Handle search logic (API call or filter results)
            updateTransportOptions();
        });

        return view;
    }

    // Dummy Data for RecyclerView (Replace with API call)
    private List<TransportOption> getDummyTransportOptions() {
        List<TransportOption> options = new ArrayList<>();
        options.add(new TransportOption("8:11 am - 9:13 am", "1h 02Min", "Bus 85 + Metro Red", "AED 5.00"));
        options.add(new TransportOption("8:13 am - 9:21 am", "1h 08Min", "Bus F42 + Metro Red", "AED 5.00"));
        options.add(new TransportOption("8:16 am - 9:23 am", "1h 07Min", "Bus F42 + Metro Red", "AED 5.00"));
        options.add(new TransportOption("8:19 am - 9:26 am", "1h 07Min", "Bus F43 + Metro Red", "AED 5.00"));
        return options;
    }

    // Function to update transport options (Placeholder)
    private void updateTransportOptions() {
        // You can fetch new data from API and update the RecyclerView
        transportAdapter.notifyDataSetChanged();
    }
}
