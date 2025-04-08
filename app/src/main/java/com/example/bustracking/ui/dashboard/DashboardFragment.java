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
import android.widget.EditText;
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
    private EditText  et_student_name, et_roll_no;

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
        et_student_name = binding.etStudentName;
        et_roll_no = binding.etRollNo;
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
            String studentName = et_student_name.getText().toString().trim();
            String rollNo = et_roll_no.getText().toString().trim();
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
            intent.putExtra("studentName", studentName);
            intent.putExtra("rollNo", rollNo);
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
                                                    "Vivekanand Nagar", "Dhavleshwar", "Kalambi", "Panchling nagar", "Bhalawani",
                                                    "Khambale", "balavdi fata", "Andhali fata", "Bambvade",
                                                    "CBSC", "Nevri", "Bhikwdi fata", "Yetgaon", "Kotij", "Kheradwangi", "Chorade", "Mhasurne", "Kamathi", "Nimsod", "Morale", "Gundewadi ", "Chitali fata", "Chitali",
                                                    "Karve", "Mangrul", "Bamani", "Dhamni", "Hatnoli", "Manjarde", "Aaravade", "Nave Dorle", "Balagavde", "Bastavade", "Khujgoan", "Vaghpur", "Kaulage", "savalaj", "Siddhewadi", "Dhahiwadi",
                                                    "Palus", "Palus Colony", "Kirloskarwadi", "Dudhondi",
                                                    "Renavi", "Aainwadi", "Jakhinwadi", "Tamkhadi", "Khanapur",
                                                    "Sultangade", "Bhivghat", "Nelkaranji", "Autevadi", "Dhavadwadi", "Kharsundi",
                                                    "Tandalgaon", "Balavadi", "Balavadi fata", "Kundal",
                                                    "Sandagewadi", "Aamnapur", "Burli",
                                                    "Bhabarde", "Devnagar fata", "Jondhalkhindi fata", "Petrol pump", "Lengre School", "Lengre", "Madhalmuthi", "Devikhindi (admin)", "Devikhindi kaman", "Kaledhon Fata","Bhikavadi Fata", "Vejegaon","Shivshakti Dhaba", "Stand Fata","Atkarimala", "High School", "Vikhale fata", "Atil Vikhale fata","Main Chowk", "Mayani",
                                                    "Aalate", "Limb","Borgaon", "Panamalewadi","Shirgaon","Visapur","Vanjarwadi","Tasgaon",
                                                    "Vita Bypass","Suryanagar", "Shigaon fata", "Shigaon", "Deorashtra","Mohite Vadgaon","Asad fata","Ambak Chichni", "Ambak Fata", "Vangi", "Shelakbav","Vadiyeraybag"});
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
        routePrices.put("AITRC (Vita) - Kadegaon", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - Kadepur", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Kalambi", new int[]{650, 1300, 1950});  //true
        routePrices.put("AITRC (Vita) - Dhavleshwar", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - Hingangaon Kh", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Nivri Naka", new int[]{500, 900, 1300});
        routePrices.put("AITRC (Vita) - Aambegaon", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - Hanmantvadiye", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Yevledi", new int[]{650, 1300, 1950});
        routePrices.put("AITRC (Vita) - Shivni", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - Aamrapur", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Soholi", new int[]{500, 900, 1300});
        routePrices.put("AITRC (Vita) - Vivekanand Nagar", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - Panchling nagar", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Bhalawani", new int[]{650, 1300, 1950});
        routePrices.put("AITRC (Vita) - Khambale", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - balavdi fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Andhali fata", new int[]{500, 900, 1300});
        routePrices.put("AITRC (Vita) - Bambvade", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - CBSC", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Nevri", new int[]{650, 1300, 1950});
        routePrices.put("AITRC (Vita) - Bhikwdi fata", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - Yetgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kotij", new int[]{500, 900, 1300});
        routePrices.put("AITRC (Vita) - Kheradwangi", new int[]{1000, 1900, 2800});
        routePrices.put("AITRC (Vita) - Chorade", new int[]{1500, 2900, 4200});
        routePrices.put("AITRC (Vita) - Mhasurne", new int[]{650, 1300, 1950});
        routePrices.put("AITRC (Vita) - Kamathi", new int[]{130, 360, 690});
        routePrices.put("AITRC (Vita) - Nimsod", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Morale", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Gundewadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Chitali fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Chitali", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Karve", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Mangrul", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Bamani", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Dhamni", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Hatnoli", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Manjarde", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Aaravade", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Nave Dorle", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Balagavde", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Bastavade", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Khujgoan", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vaghpur", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kaulage", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - savalaj", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Siddhewadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Dhahiwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Palus", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Palus Colony", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kirloskarwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Dudhondi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Renavi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Aainwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Jakhinwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Tamkhadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Khanapur", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Sultangade", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Bhivghat", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Nelkaranji", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Autevadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Dhavadwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kharsundi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Tandalgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Balavadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Balavadi fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kundal", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Sandagewadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Aamnapur", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Burli", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Bhabarde", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Devnagar fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Jondhalkhindi fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Petrol pump", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Lengre School", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Lengre", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Madhalmuthi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Devikhindi (admin)", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Devikhindi kaman", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Kaledhon Fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Bhikavadi Fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vejegaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Shivshakti Dhaba", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Stand Fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Atkarimala", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - High School", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vikhale fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Atil Vikhale fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Main Chowk", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Mayani", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Aalate", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Limb", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Borgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Panamalewadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Shirgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Visapur", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vanjarwadi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Tasgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vita Bypass", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Suryanagar", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Shigaon fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Shigaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Deorashtra", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Mohite Vadgaon", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Asad fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Ambak Chichni", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Ambak Fata", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vangi", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Shelakbav", new int[]{1300, 2600, 4800});
        routePrices.put("AITRC (Vita) - Vadiyeraybag", new int[]{1300, 2600, 4800});
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
