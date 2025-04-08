package com.example.bustracking;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CameraFragment extends Fragment {

    private Button generatePassBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        generatePassBtn = view.findViewById(R.id.btn_generate_pass); // Make sure this ID exists in your XML

        generatePassBtn.setOnClickListener(v -> checkIfPassExists());

        return view;
    }

    private void checkIfPassExists() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference("tickets");

        ticketsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Pass already exists
                    showContactTeacherDialog();
                } else {
                    // No pass, go to pass creation screen
                    startActivity(new Intent(getContext(), TicketDetailsActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error checking pass status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showContactTeacherDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Pass Already Exists")
                .setMessage("You have already generated a monthly pass. Please contact your Teacher or Department to generate a new one.")
                .setPositiveButton("OK", null)
                .show();
    }
}
