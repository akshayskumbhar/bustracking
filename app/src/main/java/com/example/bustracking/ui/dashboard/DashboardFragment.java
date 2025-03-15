package com.example.bustracking.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bustracking.R;
import com.example.bustracking.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private AutoCompleteTextView etSelectRoute;
    private AutoCompleteTextView etEndingStop;
    private RadioGroup radioGroup;
    private Button btnDecreaseFull, btnIncreaseFull, btnPay;
    private TextView tvFullCount, tvDateTime, textView3;
    private Spinner spinnerPaymentOptions;
    private int fullTicketCount = 0;
    private Handler dateTimeHandler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize Views
        etSelectRoute = binding.etSelectRoute; // EditText for route selection
        etEndingStop = binding.etEndingStop; // EditText for ending stop (hidden initially)
        radioGroup = binding.radioGroup; // RadioGroup for fare or ending stop
        btnDecreaseFull = binding.btnDecreaseFull; // Button to decrease ticket count
        btnIncreaseFull = binding.btnIncreaseFull; // Button to increase ticket count
        tvFullCount = binding.tvFullCount; // TextView showing ticket count
        spinnerPaymentOptions = binding.spinnerPaymentOptions; // Spinner for payment options
        btnPay = binding.btnPay; // Pay Button
        tvDateTime = binding.tvDateTime; // TextView for live date and time

        // Add suggestions for routes
        setupRouteSuggestions();

        // Display live date and time
        updateDateTime();

        // Setup RadioGroup listener
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_by_ending_stop) {
                etEndingStop.setVisibility(View.VISIBLE);
            } else {
                etEndingStop.setVisibility(View.GONE);
            }
        });

        // Increase/Decrease Ticket Count
        btnIncreaseFull.setOnClickListener(v -> {
            fullTicketCount++;
            tvFullCount.setText(String.valueOf(fullTicketCount));
            updatePayButtonState();
        });

        btnDecreaseFull.setOnClickListener(v -> {
            if (fullTicketCount > 0) {
                fullTicketCount--;
                tvFullCount.setText(String.valueOf(fullTicketCount));
                updatePayButtonState();
            }
        });

        // Pay Button Click Listener
        btnPay.setOnClickListener(v -> {
            // Add payment handling logic here
        });

        return root; // Return the root view
    }

    private void setupRouteSuggestions() {
        // Sample routes for suggestions
        String[] routes = {"Devikhindi", "Kadegaon", "Sangli", "Khanapur", "Kharsundi"};

        // Adapter to populate suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                routes
        );

        etSelectRoute.setAdapter(adapter); // Attach the adapter to AutoCompleteTextView

        // Optional: Show suggestions dropdown as soon as user focuses on the search box
        etSelectRoute.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etSelectRoute.showDropDown();
            }
        });
    }

    private void updateDateTime() {
        // Runnable to update the date and time every second
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy | hh:mm a", Locale.getDefault());
                String currentDateTime = sdf.format(new Date());
                tvDateTime.setText(currentDateTime);
                dateTimeHandler.postDelayed(this, 1000);
            }
        };
        dateTimeHandler.post(runnable);
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
