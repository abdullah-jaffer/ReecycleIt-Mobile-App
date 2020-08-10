package org.tensorflow.lite.examples.detection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {
    ArrayList<Alert> alerts;
    HashMap<String, String> map;
    OnItemClickListener onItemClickListener;
    public AlertAdapter(ArrayList<Alert> alerts, OnItemClickListener onItemClickListener){
        this.alerts = alerts;
        this.onItemClickListener = onItemClickListener;
        map = new HashMap<>();
        map.put("PER", "Personal");
        map.put("PUB", "Public");
        map.put("SAV", "Unreported");
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alert_row, parent, false);
        return new AlertViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        String[] items = alert.getItemList().split(",");

        holder.count.setText(Integer.toString(items.length));
        holder.status.setText(map.get(alert.getType()));
        holder.date.setText(alert.getDate());
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView count;
        TextView status;
        TextView date;
        OnItemClickListener onItemClickListener;

        public AlertViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            count = itemView.findViewById(R.id.count);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }
}
