package com.example.bustracking;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;


import java.util.Date;
import java.util.Locale;

public class TicketDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvTicketInfo = findViewById(R.id.tvTicketInfo);
        TextView tvValidity = findViewById(R.id.tvValidity);

        // Get data from Intent
        String start = getIntent().getStringExtra("start");
        String end = getIntent().getStringExtra("end");
        String duration = getIntent().getStringExtra("duration");
        int price = getIntent().getIntExtra("price", 0);
        long expiryTime = getIntent().getLongExtra("expiryTime", 0);

        // Format expiry date
        String expiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(expiryTime));
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Build ticket details
        String info = "Route: " + start + " To " + end + "\n" +
                "Duration: " + duration + "\n" +
                "Price: ₹" + price + "\n" +
                "Expiry Date: " + expiryDate;
        tvTicketInfo.setText(info);

        // Check validity
        if (System.currentTimeMillis() > expiryTime) {
            tvValidity.setText("Pass Status: ❌ Expired");
            tvValidity.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvValidity.setText("Pass Status: ✅ Valid");
            tvValidity.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

    }
}