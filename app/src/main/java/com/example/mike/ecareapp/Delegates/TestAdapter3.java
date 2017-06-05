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
import com.example.mike.ecareapp.Fragments.DoctorAppoitmentSchedule;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.DoctorAppointmentItem;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;

import java.util.List;

/**
 * Created by mike on 6/5/17.
 */

public class TestAdapter3 extends RecyclerView.Adapter<TestAdapter3.MyViewHolder> {

    private final List<PatientItem> patientItems;
    private final LayoutInflater inflater;
    private Context context;

    public TestAdapter3(Context context, List<PatientItem> patientItems){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.patientItems = patientItems;
    }

    @Override
    public TestAdapter3.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TestAdapter3.MyViewHolder myViewHolder = new TestAdapter3.MyViewHolder(inflater.inflate(R.layout.patientitem,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(TestAdapter3.MyViewHolder viewHolder, int position) {

        PatientItem homeItem = patientItems.get(position);
        Log.d("doctor adapter",homeItem.getName());

        viewHolder.name.setText(homeItem.getName());
        viewHolder.location.setText(homeItem.getLocation());
        viewHolder.email.setText(homeItem.getEmail());

        viewHolder.rippleView.setRippleColor(R.color.colorAccent);
        viewHolder.rippleView.setCentered(true);
        viewHolder.rippleView.setRippleDuration(150);
        viewHolder.rippleView.setRippleType(RippleView.RippleType.RECTANGLE);

        viewHolder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                //AppointmentBookingFragment bookingFragment = AppointmentBookingFragment.newInstance(homeItem.getDoc_id(),homeItem.getName(),homeItem.getHospital());
                //navigationInterface.fragmentNavigation(bookingFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, email, location;
        RippleView rippleView;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPatientname);
            email = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            location = (TextView) itemView.findViewById(R.id.tvPatientLocation);

            rippleView = (RippleView) itemView.findViewById(R.id.ripMainItem);
        }
    }
}
