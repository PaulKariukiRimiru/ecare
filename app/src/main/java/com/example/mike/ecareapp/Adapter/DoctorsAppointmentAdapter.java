package com.example.mike.ecareapp.Adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.example.mike.ecareapp.Fragments.SecondaryPages.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.R;

import java.util.List;

/**
 * Created by mike on 6/5/17.
 */

public class DoctorsAppointmentAdapter extends RecyclerView.Adapter<DoctorsAppointmentAdapter.MyViewHolder> {

    private final List<DoctorAppointmentItem> appointmentList;
    private final LayoutInflater inflater;
    private Context context;
    private NavigationInterface navigationInterface;

    public DoctorsAppointmentAdapter(Context context, List<DoctorAppointmentItem> appiontmentItemList, NavigationInterface navigationInterface){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.appointmentList = appiontmentItemList;
        this.navigationInterface = navigationInterface;
    }

    @Override
    public DoctorsAppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(inflater.inflate(R.layout.appointmentitem,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(DoctorsAppointmentAdapter.MyViewHolder viewHolder, int position) {
        final DoctorAppointmentItem appointmentItem = appointmentList.get(position);

        viewHolder.patientName.setText(appointmentItem.getPat_id());
        viewHolder.email.setText(appointmentItem.getPat_id());
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
                DoctorAppoitmentSchedule doctorAppoitmentSchedule = DoctorAppoitmentSchedule.newInstance(appointmentItem.getAppoint_id(),appointmentItem.getHospital(), appointmentItem.getTreatment(), appointmentItem.getPat_id(),appointmentItem.getTime(), appointmentItem.getDate());
                navigationInterface.fragmentNavigation(doctorAppoitmentSchedule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder{

         LinearLayout statusShow, statusIndicator;
         AppCompatTextView patientName, email, date, treatment, status;
         RippleView rippleView;
         public MyViewHolder(View itemView) {
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
