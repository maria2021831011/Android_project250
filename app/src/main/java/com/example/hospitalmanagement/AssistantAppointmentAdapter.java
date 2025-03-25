package com.example.hospitalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AssistantAppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PENDING = 1;
    private static final int TYPE_COMPLETED = 2;

    public interface OnAppointmentActionListener {
        void onApprove(Appointment appointment);
        void onReject(Appointment appointment);
    }

    private List<Appointment> appointmentList;
    private OnAppointmentActionListener listener;

    public AssistantAppointmentAdapter(List<Appointment> appointmentList, OnAppointmentActionListener listener) {
        this.appointmentList = appointmentList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Appointment appointment = appointmentList.get(position);
        if (appointment.getStatus().equals("Pending")) {
            return TYPE_PENDING;
        } else {
            return TYPE_COMPLETED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PENDING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_pending, parent, false);
            return new PendingAppointmentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_completed, parent, false);
            return new CompletedAppointmentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        if (holder instanceof PendingAppointmentViewHolder) {
            ((PendingAppointmentViewHolder) holder).bind(appointment, listener);
        } else if (holder instanceof CompletedAppointmentViewHolder) {
            ((CompletedAppointmentViewHolder) holder).bind(appointment);
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void updateAppointments(List<Appointment> appointments) {
        this.appointmentList.clear();
        this.appointmentList.addAll(appointments);
        notifyDataSetChanged();
    }

    public static class PendingAppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView;
        Button approveButton, rejectButton;

        public PendingAppointmentViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(Appointment appointment, OnAppointmentActionListener listener) {
            dateTextView.setText("Date: " + appointment.getDate());
            timeTextView.setText("Time: " + appointment.getTime());
            approveButton.setOnClickListener(v -> listener.onApprove(appointment));
            rejectButton.setOnClickListener(v -> listener.onReject(appointment));
        }
    }

    public static class CompletedAppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView;

        public CompletedAppointmentViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Appointment appointment) {
            dateTextView.setText("Date: " + appointment.getDate());
            timeTextView.setText("Time: " + appointment.getTime());
        }
    }
}
