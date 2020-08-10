package org.tensorflow.lite.examples.detection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertConfirmationAdapter extends RecyclerView.Adapter<AlertConfirmationAdapter.AlertConfirmationViewHolder>  {

    ArrayList<AlertConfirmation> alerts;
    OnItemClickListener onItemClickListener;

    public AlertConfirmationAdapter(ArrayList<AlertConfirmation> alerts, OnItemClickListener onItemClickListener) {
        this.alerts = alerts;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AlertConfirmationAdapter.AlertConfirmationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alert_info_row,parent, false);
        return new AlertConfirmationViewHolder(view, (OnItemClickListener) onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertConfirmationAdapter.AlertConfirmationViewHolder holder, int position) {
        AlertConfirmation alert= alerts.get(position);
        holder.date.setText(alert.getDate());
        holder.imageView.setImageResource(R.drawable.ic_add_alert_black_24dp);
    }


    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public class AlertConfirmationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date;
        ImageView imageView;
        AlertConfirmationAdapter.OnItemClickListener onItemClickListener;

        public AlertConfirmationViewHolder(View itemView, AlertConfirmationAdapter.OnItemClickListener onItemClickListener){
            super(itemView);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageView4);
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
