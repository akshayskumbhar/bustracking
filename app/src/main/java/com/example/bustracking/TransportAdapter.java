package com.example.bustracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {

    private List<TransportOption> transportOptions;

    public TransportAdapter(List<TransportOption> transportOptions) {
        this.transportOptions = transportOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transport_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransportOption option = transportOptions.get(position);
        holder.timeTextView.setText(option.getTime());
        holder.durationTextView.setText(option.getDuration());
        holder.transportModeTextView.setText(option.getTransportMode());
        holder.priceTextView.setText(option.getPrice());
    }

    @Override
    public int getItemCount() {
        return transportOptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, durationTextView, transportModeTextView, priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_text);
            durationTextView = itemView.findViewById(R.id.duration_text);
            transportModeTextView = itemView.findViewById(R.id.transport_mode_text);
            priceTextView = itemView.findViewById(R.id.price_text);
        }
    }
}
