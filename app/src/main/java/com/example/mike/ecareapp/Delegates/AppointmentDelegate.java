package com.example.mike.ecareapp.Delegates;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.Fragments.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.R;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

/**
 * Created by Mike on 4/27/2017.
 */

public class AppointmentDelegate extends AdapterDelegate<List<MainObject>> {

    private final Context context;
    private final LayoutInflater inflater;
    private final NavigationInterface navigationInterface;

    public AppointmentDelegate(Context context, NavigationInterface navigationInterface){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.navigationInterface = navigationInterface;
    }

    @Override
    protected boolean isForViewType(@NonNull List<MainObject> items, int position) {
        return items.get(position) instanceof AppiontmentItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new AppointmentDelegateViewHolder(inflater.inflate(R.layout.appointmentitem,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MainObject> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        final AppiontmentItem appointmentItem = (AppiontmentItem) items.get(position);
        AppointmentDelegateViewHolder viewHolder = (AppointmentDelegateViewHolder) holder;

        Log.d("AppointmentDelegate", appointmentItem.getTreatment());

        viewHolder.doctorName.setText(appointmentItem.getDoc_id());
        viewHolder.hospitalName.setText(appointmentItem.getHospital());
        viewHolder.date.setText("On "+ appointmentItem.getDate()+ " At "+ appointmentItem.getTime());
        viewHolder.treatment.setText(appointmentItem.getTreatment());

        viewHolder.rippleView.setCentered(true);
        viewHolder.rippleView.setRippleDuration(150);
        viewHolder.rippleView.setRippleType(RippleView.RippleType.RECTANGLE);

        if (Integer.parseInt(appointmentItem.getStatus()) == 0){
            viewHolder.status.setText("Not Confirmed");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.colorOrange));
            viewHolder.statusShow.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
            viewHolder.statusIndicator.setBackground(context.getResources().getDrawable(R.drawable.orang_circle));
            viewHolder.rippleView.setRippleColor(R.color.colorOrange);
        }else if (Integer.parseInt(appointmentItem.getStatus()) == 1){
            viewHolder.status.setText("Confirmed");
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.colorGreen));
            viewHolder.statusShow.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            viewHolder.statusIndicator.setBackground(context.getResources().getDrawable(R.drawable.gree_circle));
            viewHolder.rippleView.setRippleColor(R.color.colorGreen);
        }

        viewHolder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

            }
        });
    }

    private class AppointmentDelegateViewHolder extends RecyclerView.ViewHolder{
        LinearLayout statusShow, statusIndicator;
        AppCompatTextView doctorName, hospitalName, date, treatment, status;
        RippleView rippleView;
        public AppointmentDelegateViewHolder(View itemView) {
            super(itemView);
            statusShow = (LinearLayout) itemView.findViewById(R.id.statusShow);
            statusIndicator = (LinearLayout) itemView.findViewById(R.id.statusIndicator);

            doctorName = (AppCompatTextView) itemView.findViewById(R.id.tvdoctor);
            hospitalName = (AppCompatTextView) itemView.findViewById(R.id.tvhospital);
            date = (AppCompatTextView) itemView.findViewById(R.id.tvdate);
            treatment = (AppCompatTextView) itemView.findViewById(R.id.tvtreatment);
            status = (AppCompatTextView) itemView.findViewById(R.id.tvstatus);
            rippleView = (RippleView) itemView.findViewById(R.id.ripAppointment);
        }
    }

}
