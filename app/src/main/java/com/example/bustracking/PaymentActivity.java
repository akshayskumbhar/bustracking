package com.example.bustracking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    TextView tvSummary;
    Button btn_confirm_payment;


    String start, end, duration;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase setup
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ticketsRef = database.getReference("tickets");

        // Get passed data
        Intent intent = getIntent();
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        duration = intent.getStringExtra("duration");
        price = intent.getIntExtra("price", 0);

        Button btn_confirm_payment = findViewById(R.id.btn_confirm_payment);
        btn_confirm_payment.setOnClickListener(v -> {
            long expiryTime = System.currentTimeMillis() + getMillisForDuration(duration);

            // Save ticket to Firebase
            String ticketId = ticketsRef.push().getKey(); // generate a new key
            Ticket ticket = new Ticket(start, end, duration, price, expiryTime);
            if (ticketId != null) {
                ticketsRef.child(ticketId).setValue(ticket)
                        .addOnSuccessListener(unused -> {
                            // Go to TicketDetailsActivity
                            Intent showIntent = new Intent(PaymentActivity.this, TicketDetailsActivity.class);
                            showIntent.putExtra("start", start);
                            showIntent.putExtra("end", end);
                            showIntent.putExtra("duration", duration);
                            showIntent.putExtra("price", price);
                            showIntent.putExtra("expiryTime", expiryTime);
                            startActivity(showIntent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save ticket.", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private int getDurationInMonths(String duration) {
        switch (duration) {
            case "1 Month":
                return 1;
            case "3 Months":
                return 3;
            case "6 Months":
                return 6;
            default:
                return 1;
        }
    }
    // 🔽 🔽 Add this method below onCreate()
    private long getMillisForDuration(String duration) {
        switch (duration) {
            case "1 Week":
                return 7L * 24 * 60 * 60 * 1000;
            case "1 Month":
                return 30L * 24 * 60 * 60 * 1000;
            default:
                return 0;
        }
    }
}