package com.example.mike.ecareapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.mike.ecareapp.Delegates.DoctorAppointmentDelegate;
import com.example.mike.ecareapp.Delegates.HomeDelegate;
import com.example.mike.ecareapp.Delegates.PatientsAdapterDelegate;
import com.example.mike.ecareapp.Interfaces.NavigationInterface;
import com.example.mike.ecareapp.Pojo.MainObject;
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;

import java.util.List;

/**
 * Created by Mike on 4/27/2017.
 */

public class MainAdapter extends RecyclerView.Adapter {

        AdapterDelegatesManager<List<MainObject>> manager;
        List<MainObject> mainObjects;

    public MainAdapter(Context context, List<MainObject> mainObjects, NavigationInterface navigationInterface){
            this.mainObjects = mainObjects;
            manager = new AdapterDelegatesManager<>();
            //manager.addDelegate(new CategoriesDelegate(context, navigationInterface));
            manager.addDelegate(new HomeDelegate(context, navigationInterface));
            manager.addDelegate(new PatientsAdapterDelegate(context,navigationInterface));
    }

    @Override
    public int getItemViewType(int position) {
            return manager.getItemViewType(mainObjects,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return manager.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            manager.onBindViewHolder(mainObjects,position,holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
            manager.onBindViewHolder(mainObjects, position,holder, payloads);
    }

    @Override
    public int getItemCount() {
            return mainObjects.size();
    }
}
