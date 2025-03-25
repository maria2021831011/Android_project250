package com.example.hospitalmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    private List<Doctor> doctorList;
    private OnDoctorClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public DoctorAdapter(List<Doctor> doctorList, OnDoctorClickListener listener) {
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.nameTextView.setText(doctor.getUsername());
        String roleText = doctor.getRole().equalsIgnoreCase("assistant") ? "Assistant" : "Doctor";
        holder.nameTextView.setText(doctor.getUsername() + " (" + roleText + ")");

        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
            listener.onDoctorClick(doctor);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.doctorNameTextView);
        }
    }
}

