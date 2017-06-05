package com.example.mike.ecareapp.Delegates;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.mike.ecareapp.Fragments.AppointmentBookingFragment;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.DoctorItem;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;

import java.util.List;

/**
 * Created by mike on 6/5/17.
 */

public class TestAdapter4 extends RecyclerView.Adapter<TestAdapter4.MyViewHolder> {

    private final List<DoctorItem> patientItems;
    private final LayoutInflater inflater;
    private Context context;
    NavigationInterface navigationInterface;

    public TestAdapter4(Context context, List<DoctorItem> patientItems, NavigationInterface navigationInterface){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.patientItems = patientItems;
        this.navigationInterface = navigationInterface;
    }

    @Override
    public TestAdapter4.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TestAdapter4.MyViewHolder myViewHolder = new TestAdapter4.MyViewHolder(inflater.inflate(R.layout.homeitem,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(TestAdapter4.MyViewHolder viewHolder, int position) {

        final DoctorItem homeItem = patientItems.get(position);


        viewHolder.name.setText(homeItem.getName());
        viewHolder.specialty.setText(homeItem.getSpecialty());
        viewHolder.hospital.setText(homeItem.getHospital());

        viewHolder.rippleView.setRippleColor(R.color.colorAccent);
        viewHolder.rippleView.setCentered(true);
        viewHolder.rippleView.setRippleDuration(150);
        viewHolder.rippleView.setRippleType(RippleView.RippleType.RECTANGLE);

        viewHolder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                AppointmentBookingFragment bookingFragment = AppointmentBookingFragment.newInstance(homeItem.getDoc_id(),homeItem.getName(),homeItem.getHospital());
                navigationInterface.fragmentNavigation(bookingFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, hospital, specialty;
        RippleView rippleView;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPatientname);
            hospital = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            specialty = (TextView) itemView.findViewById(R.id.tvSpecialty);

            rippleView = (RippleView) itemView.findViewById(R.id.ripMainItem);
        }
    }
}