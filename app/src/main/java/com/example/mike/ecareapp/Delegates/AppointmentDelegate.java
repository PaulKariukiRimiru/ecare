package com.example.mike.ecareapp.Delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.ecareapp.Pojo.AppointmentItem;
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

    public AppointmentDelegate(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected boolean isForViewType(@NonNull List<MainObject> items, int position) {
        return items.get(position) instanceof AppointmentItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new AppointmentDelegateViewHolder(inflater.inflate(R.layout.appointmentitem,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MainObject> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        AppointmentItem appointmentItem = (AppointmentItem) items.get(position);
        AppointmentDelegateViewHolder viewHolder = (AppointmentDelegateViewHolder) holder;



    }

    private class AppointmentDelegateViewHolder extends RecyclerView.ViewHolder{

        public AppointmentDelegateViewHolder(View itemView) {
            super(itemView);
        }
    }

}
