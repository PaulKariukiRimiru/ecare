package com.example.mike.ecareapp.Delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.example.mike.ecareapp.Pojo.PatientItem;
import com.example.mike.ecareapp.R;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

/**
 * Created by Mike on 5/3/2017.
 */

public class PatientsAdapterDelegate extends AdapterDelegate<List<MainObject>> {

    private final Context context;
    private final LayoutInflater inflater;
    private final NavigationInterface navigationInterface;

    public PatientsAdapterDelegate(Context context, NavigationInterface navigationInterface){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.navigationInterface = navigationInterface;
    }

    @Override
    protected boolean isForViewType(@NonNull List<MainObject> items, int position) {
        return items.get(position) instanceof PatientItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PatientDelegateViewHolder(inflater.inflate(R.layout.patientitem, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MainObject> items, int position, @NonNull final RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        final PatientItem homeItem = (PatientItem) items.get(position);
        PatientDelegateViewHolder viewHolder = (PatientDelegateViewHolder) holder;

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

    private class PatientDelegateViewHolder extends RecyclerView.ViewHolder{
        TextView name, email, location;
        RippleView rippleView;
        public PatientDelegateViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvPatientname);
            email = (TextView) itemView.findViewById(R.id.tvPatientEmail);
            location = (TextView) itemView.findViewById(R.id.tvPatientLocation);

            rippleView = (RippleView) itemView.findViewById(R.id.ripMainItem);
        }
    }

}
