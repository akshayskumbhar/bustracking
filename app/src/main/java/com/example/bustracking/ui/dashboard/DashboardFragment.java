package com.example.bustracking.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bustracking.PaymentActivity;
import com.example.bustracking.R;
import com.example.bustracking.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private AutoCompleteTextView etSelectRoute, etEndingStop;
    private RadioGroup radioGroup;
    private Button btnPay, btnGenerateTicket;
    private TextView  tvDateTime, tvTicketDetails, tvTotalPrice;
    private Spinner  passDurationSpinner;
    private int fullTicketCount = 0;

    private final Map<String, String[]> routesMap = new HashMap<>();
    private final Map<String, int[]> routePrices = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        etSelectRoute = binding.etSelectRoute;
        etEndingStop = binding.etEndingStop;
        radioGroup = binding.radioGroup;
        btnPay = binding.btnPay;
        tvDateTime = binding.tvDateTime;
        btnGenerateTicket = binding.btnGenerateTicket;
        tvTicketDetails = binding.tvTicketDetails;
        tvTotalPrice = binding.tvTotalPrice;
        passDurationSpinner = binding.passDurationSpinner;

        setupRoutesData();
        setupRouteSuggestions();
        setupRoutePrices();
        updateDateTime();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_by_ending_stop) {
                etEndingStop.setVisibility(View.VISIBLE);
            } else {
                etEndingStop.setVisibility(View.GONE);
            }
        });


        btnPay.setOnClickListener(v -> {
            String start = etSelectRoute.getText().toString().trim();
            String end = etEndingStop.getText().toString().trim();

            int durationIndex = passDurationSpinner.getSelectedItemPosition();

            String duration = passDurationSpinner.getSelectedItem().toString();
            Toast.makeText(getContext(), "Pay button clicked", Toast.LENGTH_SHORT).show();
            String routeKey = start + " - " + end;
            if (!routePrices.containsKey(routeKey)) {
                Toast.makeText(requireContext(), "Invalid route selected.", Toast.LENGTH_SHORT).show();
                return;
            }

            int price = routePrices.get(routeKey)[durationIndex];

            Intent intent = new Intent(requireContext(), PaymentActivity.class);
            intent.putExtra("start", start);
            intent.putExtra("end", end);
            intent.putExtra("duration", duration);
            intent.putExtra("price", price);
            startActivity(intent);
        });

        btnGenerateTicket.setOnClickListener(v -> generateTicket());

        String[] passDurations = {"1 Month", "3 Months", "6 Months"};
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                passDurations
        );
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        passDurationSpinner.setAdapter(durationAdapter);

        return root;
    }

    private void setupRoutesData() {
        routesMap.put("AITRC (Vita)", new String[]{"Nivri Naka", "Aambegaon", "Hanmantvadiye", "Yevledi", "Shivni", "Aamrapur", "Hingangaon Kh", "Kadepur", "Soholi", "Kadegaon",
                                                    "Vivekanand Nagar", "Dhavleshwar", "Kalambi", "Panchling nagar", "Bhalawani"});
    }

    private void setupRouteSuggestions() {
        String[] routes = routesMap.keySet().toArray(new String[0]);
        ArrayAdapter<String> routeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                routes
        );
        etSelectRoute.setAdapter(routeAdapter);
        etSelectRoute.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etSelectRoute.showDropDown();
            }
        });
        etSelectRoute.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRoute = (String) parent.getItemAtPosition(position);
            updateEndingStopOptions(selectedRoute);
        });
    }

    private void updateEndingStopOptions(String selectedRoute) {
        if (routesMap.containsKey(selectedRoute)) {
            String[] endingStops = routesMap.get(selectedRoute);
            ArrayAdapter<String> endingStopAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    endingStops
            );
            etEndingStop.setAdapter(endingStopAdapter);
            etEndingStop.setText("");
        }
    }

    private void setupRoutePrices() {
        routePrices.put("AITRC (Vita) - Bhalawani", new int[]{500, 900, 1300});
        routePrices.put("AITRC (Vita) - Kadegaon", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - Kadepur", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Kalambi", new int[]{220, 440, 680});
        routePrices.put("AITRC (Vita) - Dhavleshwar", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - Hingangaon Kh", new int[]{1300, 2600, 4800});
    }

    private void generateTicket() {
        String start = etSelectRoute.getText().toString().trim();
        String end = etEndingStop.getText().toString().trim();

        if (start.isEmpty() || end.isEmpty()) {
            tvTicketDetails.setText("Please select both starting and ending routes.");
            return;
        }

        String routeKey = start + " - " + end;

        if (!routePrices.containsKey(routeKey)) {
            tvTicketDetails.setText("Invalid Route Selection.");
            return;
        }

        // Check if passDurationSpinner is populated
        if (passDurationSpinner.getSelectedItem() == null) {
            tvTicketDetails.setText("Please select a pass duration.");
            return;
        }

        int durationIndex = passDurationSpinner.getSelectedItemPosition();

        if (durationIndex < 0 || durationIndex >= routePrices.get(routeKey).length) {
            tvTicketDetails.setText("Invalid duration selected.");
            return;
        }

        int price = routePrices.get(routeKey)[durationIndex];

        String ticketDetails = "Route: " + start + " TO " + end + "\n" +
                "Duration: " + passDurationSpinner.getSelectedItem().toString() + "\n" +
                "Price: ₹" + price;

        tvTicketDetails.setText(ticketDetails);
        tvTotalPrice.setText("Total: ₹" + price);
    }




    private void updateDateTime() {
        tvDateTime.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy | hh:mm a", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());
                tvDateTime.setText(currentDateTime);
                tvDateTime.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updatePayButtonState() {
        boolean isRouteFilled = !etSelectRoute.getText().toString().isEmpty();
        boolean isEndingStopFilled = radioGroup.getCheckedRadioButtonId() == R.id.rb_by_ending_stop &&
                !etEndingStop.getText().toString().isEmpty();

        btnPay.setEnabled(isRouteFilled && (isEndingStopFilled || radioGroup.getCheckedRadioButtonId() == R.id.rb_by_fare));
    }
}
