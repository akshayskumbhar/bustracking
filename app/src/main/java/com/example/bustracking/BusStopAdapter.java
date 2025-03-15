package com.example.bustracking;

import android.support.v4.os.IResultReceiver;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.ViewHolder> implements Filterable {
    private List<BusStop> busStops;
    private List<BusStop> busStopsFull; // Full copy for filtering
    private OnStopClickListener listener;

    public interface OnStopClickListener {
        void onStopClick(BusStop stop);
    }

    public BusStopAdapter(List<BusStop> busStops, OnStopClickListener listener) {
        this.busStops = busStops;
        this.busStopsFull = new ArrayList<>(busStops); // Keep a copy for filtering
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        android.view.View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus_stop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusStop stop = busStops.get(position);
        holder.stopName.setText(stop.getStopName());

        holder.itemView.setOnClickListener(v -> listener.onStopClick(stop));
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<BusStop> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(busStopsFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (BusStop stop : busStopsFull) {
                        if (stop.getStopName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(stop);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                busStops.clear();
                busStops.addAll((List<BusStop>) results.values);
                notifyDataSetChanged();
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stopName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            stopName = itemView.findViewById(R.id.stop_name);
        }
    }
}