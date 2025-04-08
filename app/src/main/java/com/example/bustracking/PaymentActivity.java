package com.example.bustracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {

    TextView tvSummary;
    Button btn_confirm_payment;

    String studentName, rollNo, start, end, duration;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_payment);

        // Get passed data from previous screen
        Intent intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        rollNo = intent.getStringExtra("rollNo");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        duration = intent.getStringExtra("duration");
        price = intent.getIntExtra("price", 0);

        // Firebase setup
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ticketsRef = database.getReference("tickets");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btn_confirm_payment = findViewById(R.id.btn_confirm_payment);
        btn_confirm_payment.setOnClickListener(v -> {
            long expiryTime = System.currentTimeMillis() + getMillisForDuration(duration);

            // Save ticket using UID as key (important!)
            Ticket ticket = new Ticket(studentName, rollNo, start, end, duration, price, expiryTime);
            ticketsRef.child(userId).setValue(ticket)
                    .addOnSuccessListener(unused -> {
                        // Go to TicketDetailsActivity after saving
                        Intent showIntent = new Intent(PaymentActivity.this, TicketDetailsActivity.class);
                        showIntent.putExtra("studentName", studentName);
                        showIntent.putExtra("rollNo", rollNo);
                        showIntent.putExtra("start", start);
                        showIntent.putExtra("end", end);
                        showIntent.putExtra("duration", duration);
                        showIntent.putExtra("price", price);
                        showIntent.putExtra("expiryTime", expiryTime);
                        startActivity(showIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PaymentActivity.this, "Failed to save ticket.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private long getMillisForDuration(String duration) {
        switch (duration) {
            case "1 Week":
                return 7L * 24 * 60 * 60 * 1000;
            case "1 Month":
                return 30L * 24 * 60 * 60 * 1000;
            case "2 Months":
                return 60L * 24 * 60 * 60 * 1000;
            case "3 Months":
                return 90L * 24 * 60 * 60 * 1000;
            case "6 Months":
                return 180L * 24 * 60 * 60 * 1000;
            default:
                return 0;
        }
    }
}
