package com.example.bustracking.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bustracking.AITRC_Vita;
import com.example.bustracking.MapActivity;
import com.example.bustracking.Model;
import com.example.bustracking.R;
import com.example.bustracking.RecycleViewAdapter;
import com.example.bustracking.Vita_Bambvade;
import com.example.bustracking.Vita_Bhalavni;
import com.example.bustracking.Vita_Chitali;
import com.example.bustracking.Vita_Dahivadi;
import com.example.bustracking.Vita_Dudhondi;
import com.example.bustracking.Vita_Khanapur;
import com.example.bustracking.Vita_Kharsundi;
import com.example.bustracking.Vita_Kundal;
import com.example.bustracking.Vita_Kurli;
import com.example.bustracking.Vita_Mayni;
import com.example.bustracking.Vita_Tasgaon;
import com.example.bustracking.Vita_Vadiyeraybag;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecycleViewAdapter.OnBusStopClickListener {
    private RecycleViewAdapter adapter;
    private ArrayList<Model> modelArrayList;
    private RecyclerView recyclerView;



    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        modelArrayList = new ArrayList<>();

        recyclerView = root.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        modelArrayList.add(new Model("AITRC to Vita", "MH 10 BT 6721"));
        modelArrayList.add(new Model("Kadegaon to Vita", "MH 10 DT 1952"));
        modelArrayList.add(new Model("Bhalavani to Vita", "MH 10 DT 4437"));
        modelArrayList.add(new Model("Kurli to Vita", "MH 10 AW 5074"));
        modelArrayList.add(new Model("Dahivadi to Vita", "MH 10 DT 1801"));
        modelArrayList.add(new Model("Tasgaon to Vita", "MH 10 DT 3836"));
        modelArrayList.add(new Model("Chitali to Vita", "MH 10 DT 4432"));
        modelArrayList.add(new Model("Khanapur to Vita", "MH 10 DT 6720"));
        modelArrayList.add(new Model("Mayni to Vita", "MH 10 DT 3833"));
        modelArrayList.add(new Model("Kundal to Vita", "MH 10 DT 3834"));
        modelArrayList.add(new Model("Dudhondi to Vita", "MH 10 DT 5075"));
        modelArrayList.add(new Model("Bambvade to Vita", "MH 10 DT 6705"));
        modelArrayList.add(new Model("Kharsundi to Vita", "MH 10 DT 6034"));
        modelArrayList.add(new Model("Vadiyeraybag to Vita", "MH 10 DT 3076"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecycleViewAdapter(modelArrayList, getContext(), this);
        recyclerView.setAdapter(adapter);

        return root;
    }
    @Override
    public void onBusStopClick(Model model) {
        Intent intent;
        switch (model.getName()) {  // Customize based on the unique attribute (e.g., name or ID)
            case "Bhalavani to Vita":
                intent = new Intent(getActivity(), Vita_Bhalavni.class);
                break;
            case "Kadegaon to Vita":
                intent = new Intent(getActivity(), MapActivity.class); // MapActivity For Kadegaon to vita
                break;
            case "AITRC to Vita":
                intent = new Intent(getActivity(), AITRC_Vita.class); // AIT College to vita
                break;
            case "Kurli to Vita":
                intent = new Intent(getActivity(), Vita_Kurli.class); // Kurli to vita
                break;
            case "Dahivadi to Vita":
                intent = new Intent(getActivity(), Vita_Dahivadi.class); // Dahivadi to vita
                break;
            case "Tasgaon to Vita":
                intent = new Intent(getActivity(), Vita_Tasgaon.class); // Tasgaon to vita
                break;
            case "Chitali to Vita":
                intent = new Intent(getActivity(), Vita_Chitali.class); // Chitali to vita
                break;
            case "Khanapur to Vita":
                intent = new Intent(getActivity(), Vita_Khanapur.class); // Khanapur to vita
                break;
            case "Mayni to Vita":
                intent = new Intent(getActivity(), Vita_Mayni.class); // Mayni to vita
                break;
            case "Kundal to Vita":
                intent = new Intent(getActivity(), Vita_Kundal.class); // Kundal to vita
                break;
            case "Dudhondi to Vita":
                intent = new Intent(getActivity(), Vita_Dudhondi.class); // Dudhondi to vita
                break;
            case "Bambvade to Vita":
                intent = new Intent(getActivity(), Vita_Bambvade.class); // Bambvade to vita
                break;
            case "Kharsundi to Vita":
                intent = new Intent(getActivity(), Vita_Kharsundi.class); // Kharsundi to vita
                break;
            case "Vadiyeraybag to Vita":
                intent = new Intent(getActivity(), Vita_Vadiyeraybag.class); // Vadiyeraybag to vita
                break;
            // Add more cases for each bus stop
            default:
                intent = new Intent(getActivity(), MapActivity.class);  // Default activity
                break;
        }
        startActivity(intent);
    }
}