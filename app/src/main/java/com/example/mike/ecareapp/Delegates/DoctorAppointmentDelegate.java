package com.example.mike.ecareapp.Delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.example.mike.ecareapp.Database.DatabaseHandler;
import com.example.mike.ecareapp.Fragments.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.AppiontmentItem;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.R;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

/**
 * Created by Mike on 5/3/2017.
 */

public class DoctorAppointmentDelegate extends AdapterDelegate<List<MainObject>> {

    private final Context context;
    private final LayoutInflater inflater;
    private final NavigationInterface navigationInterface;

    public DoctorAppointmentDelegate(Context context, NavigationInterface navigationInterface){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.navigationInterface = navigationInterface;
    }

    @Override
    protected boolean isForViewType(@NonNull List<MainObject> items, int position) {
        return items.get(position) instanceof DoctorAppointmentItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new DoctorAppointmentDelegateViewHolder(inflater.inflate(R.layout.appointmentitem,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MainObject> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        final DoctorAppointmentItem appointmentItem = (DoctorAppointmentItem) items.get(position);
        DoctorAppointmentDelegate.DoctorAppointmentDelegateViewHolder viewHolder = (DoctorAppointmentDelegate.DoctorAppointmentDelegateViewHolder) holder;

        viewHolder.patientName.setText(appointmentItem.getPat_id());
        viewHolder.email.setText(appointmentItem.getPat_id());
        viewHolder.date.setText("On "+ appointmentItem.getDay()+"/"+appointmentItem.getMonth()+"/"+appointmentItem.getYear()+ " At "+ appointmentItem.getHour()+":"+appointmentItem.getMinute());
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
                DoctorAppoitmentSchedule doctorAppoitmentSchedule = DoctorAppoitmentSchedule.newInstance(appointmentItem.getAppoint_id(),"");
                navigationInterface.fragmentNavigation(doctorAppoitmentSchedule);
            }
        });
    }

    private class DoctorAppointmentDelegateViewHolder extends RecyclerView.ViewHolder{
        LinearLayout statusShow, statusIndicator;
        AppCompatTextView patientName, email, date, treatment, status;
        RippleView rippleView;
        public DoctorAppointmentDelegateViewHolder(View itemView) {
            super(itemView);
            statusShow = (LinearLayout) itemView.findViewById(R.id.statusShow);
            statusIndicator = (LinearLayout) itemView.findViewById(R.id.statusIndicator);

            patientName = (AppCompatTextView) itemView.findViewById(R.id.tvdoctor);
            email = (AppCompatTextView) itemView.findViewById(R.id.tvhospital);
            date = (AppCompatTextView) itemView.findViewById(R.id.tvdate);
            treatment = (AppCompatTextView) itemView.findViewById(R.id.tvtreatment);
            status = (AppCompatTextView) itemView.findViewById(R.id.tvstatus);

            rippleView = (RippleView) itemView.findViewById(R.id.ripAppointment);
        }
    }

}
